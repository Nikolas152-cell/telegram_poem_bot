import jdk.jfr.internal.JVM;
import org.apache.commons.logging.Log;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;
import java.util.logging.Level;

public class Bot extends TelegramLongPollingBot {

    private String message;
    private String chatId;
    private String new_url; // to find poem of author
    Parser parser;
    Map<String, ArrayList<String>> diaryOfPoems;

    Bot()
    {
        List<String> poemsLove = new ArrayList<>();

    }

    //method for filling our map



    @Override
    public String getBotUsername() {
        return "@poem_bot_for_kolya_bot";
    }

    @Override
    public String getBotToken() {
        return "2095772285:AAF-kHsKCiOLEU5nuJNRH4JzzLkjgqv5v0A";
    }

    ////33 row

   @Override
    public void onUpdateReceived(Update update) {
        parser = new Parser();
        message = update.getMessage().getText();
        chatId = update.getMessage().getChatId().toString();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        String text = update.getMessage().getText();

        //first try/catch if message is not long
        try{
            sendMessage.setText(getMessage(text));
            execute(sendMessage);
        }catch (TelegramApiException e)
        {
            //second try/catch if long, but can be divided
            try {
                String firstHalf, secondHalf, thirdHalf, fourthHalf, fifth, sixth;
                firstHalf = sendMessage.getText().substring(sendMessage.getText().length()/(4),
                        sendMessage.getText().length()/3);
                secondHalf = sendMessage.getText().substring(sendMessage.getText().length()/(3),
                        sendMessage.getText().length()/2);

                thirdHalf = sendMessage.getText().substring(sendMessage.getText().length()/(2), //temporary string for storing data
                        sendMessage.getText().length());
                fourthHalf = thirdHalf.substring(0,
                        thirdHalf.length()/3);




                sendMessage.setText(firstHalf);
                execute(sendMessage);
                sendMessage.setText(secondHalf);
                execute(sendMessage);
                sendMessage.setText(fourthHalf);
                execute(sendMessage);
            } catch (TelegramApiException ex)
            {
                //final catch if poem is too long((
                sendMessage.setText("Стих оказался очень длинным и для сбережения вашего времени" +
                        "бот не может вам его показать");
                try {
                    execute(sendMessage);
                } catch (TelegramApiException exc) {
                    exc.printStackTrace();
                }
            }
        }

    }

    public String getMessage(String message)
    {
        if(message.equals("/help"))
        {
            return "У этого бота есть такие команды: "; // не доработано
        }
        String poem;//for founded poem
        if(message.equals("/start"))
        {
            return "Введите имя автора";
        }
        if(message.equals("/random"))
        {
            poem = parser.RandomPoem();
            return poem;
        }
        if(message.equals("/topics"))
        {
            // список тем
        }

        if(parser.isAuthor(message))
        {
            new_url = parser.findAuthor(message);
            return "Да, этот автор существует." +
                    "" + parser.showAllAuthorsPoems(message) +
                    " Введите название стиха.";
        }
        if(parser.isPoem(message, new_url))
        {
            poem = parser.findPoem(message, new_url);
            return poem;
        }



        return "Не понял";
    }














    private void handleMessage(Message message, Update update){

   }
}
