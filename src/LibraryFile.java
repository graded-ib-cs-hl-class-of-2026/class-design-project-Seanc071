import java.io.BufferedReader; // read the file
import java.io.BufferedWriter; // write in the file
import java.io.FileReader; // opens the file
import java.io.FileWriter; // opens file for writing
import java.io.IOException; // use when there is an error in the file
import java.time.LocalDateTime; // get time rn
import java.time.format.DateTimeFormatter; // time format
import javax.swing.*;

// https://docs.oracle.com/javase/8/docs/api/java/io/package-summary.html

public class LibraryFile {
    private JFrame frame;// the tab used for showing error 
    private Book[] books;// array to hold book 
    private String[][] users; // 2darray to hold user id and password
    private int userCount; // number of useres stored
    private String registeredID; // Currently logged-in user

    /**this is a constructor initializeing the class with the data below */
    public LibraryFile(JFrame frame, Book[] books, String[][] users, int userCount, int requestedCount, String registeredID) {
        this.frame = frame;
        this.books = books;
        this.users = users;
        this.userCount = userCount;
        this.registeredID = registeredID;
    }


    // Sets the current logged in user ID
    public void setRegisteredID(String registeredID) {
        this.registeredID = registeredID;
    }
        // Updates the user count (used after adding a user)
    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }


        // https://chatgpt.com/share/6807c3a1-4714-8007-977e-0161a86b470c
        // write current book catalog to catalog.txt
    public void CatalogFile() {
       // opens the catalog.txt file
        // BufferedWriter make it write the text to the file efficiently
        // try... makes that the file automatically closed after writing
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("catalog.txt"))) {
            // this loop goes throguh every book in the book array
            for (int i = 0; i < books.length; i++) {
                Book book = books[i];
                String status;
                if(book.checkedout()){
                    status = "Checked out by: " + book.getBorrower();
                } else{
                    status = "Available";
                }
                // format of the writing books, %s means string
                String line = String.format("Title: %s | Author: %s | Code: %s | %s",
                        book.getTitle(), book.getAuthor(), book.getCode(), status);
                // write the upperthing in the file and change to new line
                writer.write(line);
                writer.newLine();
            }
            // saying that the thing was successfully worked
            JOptionPane.showMessageDialog(frame, "Catalog has been wrote in catalog.txt");
        }
        //to handle error(prevent crashing)
        catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error exporting catalog: " + e.getMessage());


        }}

        /**for borrowTime.txt */
    public void borrowTime(String action, Book book) {
    // basically same method as catalogfile thing
    // https://chatgpt.com/share/6807d3cb-bb20-8007-97d0-a88f01274ee8
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("borrowTime.txt", true ))) {
            // https://chatgpt.com/share/6807c558-23b8-8007-becf-74f22196f690
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String times = now.format(formatter);
            // https://chatgpt.com/share/6807c77f-79d8-8007-b04e-5314dee9c676
            String a = String.format("%s, %s, %s, %s, %s",
            times, registeredID, action, book.getTitle(), book.getCode());
            // so it write sin the file, and go next to new line
            writer.write(a);
            writer.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error writing to borrow log: " + e.getMessage());
        }
    }



    /**loading existing from the file users.txt */
    public int userFile() {
        // setting the inital user to 0
    userCount = 0; 
    try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) { 
        String line;
        // Reads one line at a time from the file.
        // https://chatgpt.com/share/6807c8f5-ae54-8007-adb1-904746581662
        while ((line = reader.readLine()) != null ) {
            // spliting each line with the |
            String[] column = line.split("\\|");
            if (column.length == 2) {
                users[userCount][0] = column[0]; // id
                users[userCount][1] = column[1]; // password
                // adding by 1 so it can check every row
                userCount++;
            }
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(frame, "Error loading users: " + e.getMessage());
    }
    return userCount;
}

    // putting in users.txt
    public void saveUser(String filename, String id, String password) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(id + "|" + password);
            writer.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error saving user: " + e.getMessage());
        }
    }
// check if there are same user id
    public boolean userChecker(String id) {
        for (int i = 0; i < userCount; i++) {
            if (users[i][0].equals(id)) {
                return true;
            }
        }
        return false;
    }

    // for books.txt read
    // https://chatgpt.com/share/68080da9-7b70-8007-aa21-1cfbd2baf8eb
    public void BooksFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("books.txt"))) {
            String line;
            int index = 0;
            while ((line = reader.readLine()) != null) {
                String[] column = line.split("\\|");
                if (column.length == 5) {
                    String title = column[0];
                    String author = column[1];
                    boolean checkedOut = Boolean.parseBoolean(column[2]);
                    String borrower = column[3];
                    String code = column[4];
                    books[index++] = new Book(title, author, checkedOut, borrower, code);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error loading books from file: " + e.getMessage());
        }
    }


    // for logins.txt read
    public void logSession(String action) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("logins.txt", true))) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String timestamp = now.format(formatter);
            String line = "[" + timestamp + "] " + registeredID + " " + action;
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error writing session log: " + e.getMessage());
        }
    }

    // for books.txt to save the data
   public void saveBooksToFile() {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter("books.txt"))) {
        for (int i = 0; i < books.length; i++) {
            Book book = books[i];
            if (book != null) { 
                String line = book.getTitle() + "|" + book.getAuthor() + "|" + book.checkedout() + "|" + book.getBorrower() + "|" + book.getCode();
                writer.write(line);
                writer.newLine();
            }
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(frame, "Error saving books: " + e.getMessage());
    }
}



    public int getUserCount() {
        return userCount;
    }
}

