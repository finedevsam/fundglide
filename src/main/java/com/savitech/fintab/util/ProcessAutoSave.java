package com.savitech.fintab.util;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.savitech.fintab.entity.Account;
import com.savitech.fintab.entity.TargetSavings;
import com.savitech.fintab.entity.TargetSavingsHistory;
import com.savitech.fintab.entity.User;
import com.savitech.fintab.repository.AccountRepository;
import com.savitech.fintab.repository.TargetSavingsHistoryRepository;
import com.savitech.fintab.repository.TargetSavingsRepository;
import com.savitech.fintab.repository.UserRepository;

import lombok.SneakyThrows;

@Component
public class ProcessAutoSave {
    @Autowired
    private TargetSavingsRepository targetSavingsRepository;

    @Autowired
    private Helper helper;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TargetSavingsHistoryRepository targetSavingsHistoryRepository;

    @Autowired
    private RandomStringGenerator randomStringGenerator;

    @Autowired
    private EmailNotification emailNotification;

    @Value("${bank_code}")
    private String bank_code;

    @SneakyThrows
    @Scheduled(cron = "1 * * * * *")
    public void autoSave(){
        Date today = new Date();
        String currentTime = processingDate(today);
        LocalDate currentDate = LocalDate.now();

        String todaysDay = processingDay(today);
        List<TargetSavings> savings = targetSavingsRepository.findAll();
        for(TargetSavings data : savings){
            String payTime = formatProcessingDate(data.getTargetSavingsConfig().getAutoSaveTime());
            
            DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
            String todayName = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault());
            switch (data.getTargetSavingsConfig().getAutoSaveType()) {
                case "daily":
                    if(Objects.equals(currentTime, payTime)){
                        User user = userRepository.findUserById(data.getCustomer().getUser().getId());
                        Account account = accountRepository.findAccountByCustomerId(data.getCustomer().getId());
                        double accountBalance = Double.valueOf(account.getBalance());
                        double amountToSave = data.getTargetSavingsConfig().getAutoSavingsAmount();
                        if(accountBalance > amountToSave){

                            TargetSavings targetSavings = targetSavingsRepository.findTargetSavingsById(data.getId());
                            double balanceAfterSave = accountBalance - amountToSave;
                            double newBal = data.getBalance() + amountToSave;

                            targetSavings.setBalance(newBal);

                            account.setBalance(String.valueOf(balanceAfterSave));

                            targetSavingsRepository.save(targetSavings);
                            accountRepository.save(account);

                            // Create Log for source Account
                            helper.createTransactionLog(
                                account.getAccountNo(), 
                                bank_code, data.getAccountNo(), 
                                bank_code, String.valueOf(amountToSave),
                                String.format("%s|AUTOSAVE", data.getTitle().toUpperCase())
                                );
                            
                            // Create Log for Target Savings
                            TargetSavingsHistory history = new TargetSavingsHistory();

                            history.setAmount(amountToSave);
                            history.setDate(today);
                            history.setTargetSavings(targetSavings);
                            history.setReference(randomStringGenerator.generateReference(16));
                            history.setCustomer(data.getCustomer());
                            targetSavingsHistoryRepository.save(history);

                            // Send email notification as regards the savings
                            emailNotification.AutoSaveEmail(
                                data.getCustomer().getFirstName(), 
                                user.getEmail(),
                                amountToSave, 
                                data.getTitle());
                        }
                    
                    }
                    break;
                case "weekly":
                    Map<Object, Object> getDay = setDaysName();
                    if(Objects.equals(getDay.get(data.getTargetSavingsConfig().getAutoSaveDay()), todayName) && Objects.equals(currentTime, payTime)){
                        User user = userRepository.findUserById(data.getCustomer().getUser().getId());
                        Account account = accountRepository.findAccountByCustomerId(data.getCustomer().getId());
                        double accountBalance = Double.valueOf(account.getBalance());
                        double amountToSave = data.getTargetSavingsConfig().getAutoSavingsAmount();
                        if(accountBalance > amountToSave){

                            TargetSavings targetSavings = targetSavingsRepository.findTargetSavingsById(data.getId());
                            double balanceAfterSave = accountBalance - amountToSave;
                            double newBal = data.getBalance() + amountToSave;

                            targetSavings.setBalance(newBal);

                            account.setBalance(String.valueOf(balanceAfterSave));

                            targetSavingsRepository.save(targetSavings);
                            accountRepository.save(account);

                            // Create Log for source Account
                            helper.createTransactionLog(
                                account.getAccountNo(), 
                                bank_code, data.getAccountNo(), 
                                bank_code, String.valueOf(amountToSave),
                                String.format("%s|AUTOSAVE", data.getTitle().toUpperCase())
                                );
                            
                            // Create Log for Target Savings
                            TargetSavingsHistory history = new TargetSavingsHistory();

                            history.setAmount(amountToSave);
                            history.setDate(today);
                            history.setTargetSavings(targetSavings);
                            history.setReference(randomStringGenerator.generateReference(16));
                            history.setCustomer(data.getCustomer());
                            targetSavingsHistoryRepository.save(history);

                            // Send email notification as regards the savings
                            emailNotification.AutoSaveEmail(
                                data.getCustomer().getFirstName(), 
                                user.getEmail(),
                                amountToSave, 
                                data.getTitle());
                        }
                    
                    }
                    break;
                
                case "monthly":
                    if(Objects.equals(currentTime, payTime) && Objects.equals(data.getTargetSavingsConfig().getAutoSaveDay(), todaysDay)){
                        User user = userRepository.findUserById(data.getCustomer().getUser().getId());
                        Account account = accountRepository.findAccountByCustomerId(data.getCustomer().getId());
                        double accountBalance = Double.valueOf(account.getBalance());
                        double amountToSave = data.getTargetSavingsConfig().getAutoSavingsAmount();
                        if(accountBalance > amountToSave){

                            TargetSavings targetSavings = targetSavingsRepository.findTargetSavingsById(data.getId());
                            double balanceAfterSave = accountBalance - amountToSave;
                            double newBal = data.getBalance() + amountToSave;

                            targetSavings.setBalance(newBal);

                            account.setBalance(String.valueOf(balanceAfterSave));

                            targetSavingsRepository.save(targetSavings);
                            accountRepository.save(account);

                            // Create Log for source Account
                            helper.createTransactionLog(
                                account.getAccountNo(), 
                                bank_code, data.getAccountNo(), 
                                bank_code, String.valueOf(amountToSave),
                                String.format("%s|AUTOSAVE", data.getTitle().toUpperCase())
                                );
                            
                            // Create Log for Target Savings
                            TargetSavingsHistory history = new TargetSavingsHistory();

                            history.setAmount(amountToSave);
                            history.setDate(today);
                            history.setTargetSavings(targetSavings);
                            history.setReference(randomStringGenerator.generateReference(16));
                            history.setCustomer(data.getCustomer());
                            targetSavingsHistoryRepository.save(history);

                            // Send email notification as regards the savings
                            emailNotification.AutoSaveEmail(
                                data.getCustomer().getFirstName(), 
                                user.getEmail(),
                                amountToSave, 
                                data.getTitle());
                        }
                    
                    }
                    break;

                default:
                    break;
            }
            
            
        }
    }

    private String processingDate(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        return formatter.format(date);
    }

    private String formatProcessingDate(LocalTime time){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String formattedTime = time.format(formatter);
        return formattedTime;
    }

    private String processingDay(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("dd");
        return formatter.format(date);
    }

    private Map<Object, Object> setDaysName(){
        Map<Object, Object> data = new HashMap<>();
        data.put("01", "Sunday");
        data.put("02", "Monday");
        data.put("03", "Tuesday");
        data.put("04", "Wednesday");
        data.put("05", "Thursday");
        data.put("06", "Friday");
        data.put("07", "Saturday");
        return data;
    }
}
