package com.studentwallet.service;

import com.studentwallet.model.dto.ExpenseRequest;
import com.studentwallet.model.dto.ExpenseResponse;
import com.studentwallet.model.dto.SmsImportResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SmsParserService {

    private static final Pattern[] AMOUNT_PATTERNS = {
            Pattern.compile("(?:Rs\\.?|INR|₹)\\s*([\\d,]+(?:\\.\\d{1,2})?)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("(?:debited|credited|spent|paid)\\s*(?:for)?\\s*(?:Rs\\.?|INR|₹)?\\s*([\\d,]+(?:\\.\\d{1,2})?)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("([\\d,]+(?:\\.\\d{1,2})?)\\s*(?:has been|is)\\s*(?:debited|credited)", Pattern.CASE_INSENSITIVE)
    };

    private static final Map<String, String> KEYWORD_CATEGORIES = new LinkedHashMap<>();

    static {
        KEYWORD_CATEGORIES.put("zomato", "Swiggy/Zomato");
        KEYWORD_CATEGORIES.put("swiggy", "Swiggy/Zomato");
        KEYWORD_CATEGORIES.put("ola", "Travel/Auto");
        KEYWORD_CATEGORIES.put("uber", "Travel/Auto");
        KEYWORD_CATEGORIES.put("rapido", "Travel/Auto");
        KEYWORD_CATEGORIES.put("auto", "Travel/Auto");
        KEYWORD_CATEGORIES.put("metro", "Travel/Auto");
        KEYWORD_CATEGORIES.put("irctc", "Travel/Auto");
        KEYWORD_CATEGORIES.put("mess", "Mess/Canteen");
        KEYWORD_CATEGORIES.put("canteen", "Mess/Canteen");
        KEYWORD_CATEGORIES.put("chai", "Chai/Coffee");
        KEYWORD_CATEGORIES.put("coffee", "Chai/Coffee");
        KEYWORD_CATEGORIES.put("starbucks", "Chai/Coffee");
        KEYWORD_CATEGORIES.put("ccd", "Chai/Coffee");
        KEYWORD_CATEGORIES.put("netflix", "Subscriptions");
        KEYWORD_CATEGORIES.put("spotify", "Subscriptions");
        KEYWORD_CATEGORIES.put("prime video", "Subscriptions");
        KEYWORD_CATEGORIES.put("hotstar", "Subscriptions");
        KEYWORD_CATEGORIES.put("photocopy", "Academic");
        KEYWORD_CATEGORIES.put("printing", "Academic");
        KEYWORD_CATEGORIES.put("book", "Academic");
        KEYWORD_CATEGORIES.put("pvr", "Entertainment");
        KEYWORD_CATEGORIES.put("inox", "Entertainment");
        KEYWORD_CATEGORIES.put("bookmyshow", "Entertainment");
    }

    private final ExpenseService expenseService;

    public SmsParserService(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    public SmsImportResponse importSms(String userId, String smsText) {
        SmsImportResponse response = new SmsImportResponse();

        BigDecimal amount = extractAmount(smsText);
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            response.setParsed(false);
            response.setMessage("Could not detect transaction amount. Paste a UPI/bank debit SMS.");
            return response;
        }

        String merchant = extractMerchant(smsText);
        String category = categorize(smsText, merchant);
        String description = merchant != null ? merchant : "UPI transaction";

        response.setParsed(true);
        response.setAmount(amount);
        response.setCategory(category);
        response.setMerchant(merchant);
        response.setDescription(description);

        ExpenseRequest request = new ExpenseRequest();
        request.setAmount(amount);
        request.setCategory(category);
        request.setDescription(description + " (SMS import)");

        try {
            ExpenseResponse expense = expenseService.addExpense(userId, request);
            response.setExpense(expense);
            response.setMessage("Imported ₹" + amount + " as " + category);
        } catch (IllegalStateException ex) {
            response.setMessage("Parsed successfully but could not save: " + ex.getMessage());
        }

        return response;
    }

    private BigDecimal extractAmount(String sms) {
        for (Pattern pattern : AMOUNT_PATTERNS) {
            Matcher matcher = pattern.matcher(sms);
            if (matcher.find()) {
                String raw = matcher.group(1).replace(",", "");
                try {
                    return new BigDecimal(raw);
                } catch (NumberFormatException ignored) {
                    // try next pattern
                }
            }
        }
        return null;
    }

    private String extractMerchant(String sms) {
        Pattern vpaPattern = Pattern.compile("(?:to|at|from)\\s+([A-Za-z0-9._-]+@[A-Za-z]+)", Pattern.CASE_INSENSITIVE);
        Matcher vpaMatcher = vpaPattern.matcher(sms);
        if (vpaMatcher.find()) {
            return vpaMatcher.group(1).split("@")[0];
        }

        Pattern merchantPattern = Pattern.compile("(?:at|to|for)\\s+([A-Z][A-Za-z0-9\\s]{2,30})", Pattern.CASE_INSENSITIVE);
        Matcher merchantMatcher = merchantPattern.matcher(sms);
        if (merchantMatcher.find()) {
            return merchantMatcher.group(1).trim();
        }

        return null;
    }

    private String categorize(String sms, String merchant) {
        String lower = sms.toLowerCase();
        if (merchant != null) {
            lower = lower + " " + merchant.toLowerCase();
        }

        for (Map.Entry<String, String> entry : KEYWORD_CATEGORIES.entrySet()) {
            if (lower.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        return "Other";
    }
}
