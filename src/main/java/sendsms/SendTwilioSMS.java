package sendsms;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.Twilio;
import com.twilio.type.PhoneNumber;
import main.Employee;

public class SendTwilioSMS {
    private static final String ACCOUNT_SID = "ACed02e298c9d27c600da6fd10bfe7fcc2";
    private static final String AUTH_TOKEN = "90ff56b4493259c882369f57a760a552";

    private String from = "+17192592416";
    private String to;
    private Employee[] employees;
    private String[] salaries;

    public SendTwilioSMS(Employee[] employees, String[] salaries) {
        this.employees = employees;
        this.salaries = salaries;
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    public int sendMessage() {
        int success = 0;
        for (int i = 0; i < employees.length; i++) {
            to = "+2" + employees[i].getPhoneNumber();
            String salary = salaries[i];
            Message.creator(
                    new PhoneNumber(to),
                    new PhoneNumber(from),
                    employees[i].getTitle() + employees[i].getName() + "\nWe\'ve added " + salary + " L.E. in your account ").create();
            success++;

        }
        return success;
    }


}
