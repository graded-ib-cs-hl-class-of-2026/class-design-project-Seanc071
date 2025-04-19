import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.*;

public class LibraryFileHandler {
    private JFrame frame;// the tab used for showing error 
    private Book[] books;// array to hold book 
    private String[][] users; // 2darray to hold user id and password
    private int userCount; // number of useres stored
    private String registeredID; // Currently logged-in user

    /**this is a constructor initializeing the class with the data below */
    public LibraryFileHandler(JFrame frame, Book[] books, String[][] users, int userCount, String[] requestedBooks, int requestedCount, String registeredID) {
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

        /**for borrowlog.txt */
    public void logBorrowAction(String action, Book book) {
    //basically same method as catalogfile thing
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("borrowlog.txt", true))) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String timestamp = now.format(formatter);
            String a = String.format("%s %s %s %s (code: %s)",
                timestamp, registeredID, action, book.getTitle(), book.getCode());
            writer.write(a);
            writer.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error writing to borrow log: " + e.getMessage());
        }
    }



    /**calling users.txt */
    public int loadUsersFromFile() {
        // setting the inital user to 0
    userCount = 0; 
    try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) { 
        String line;
        // Reads one line at a time from the file.
        while ((line = reader.readLine()) != null && userCount < users.length) {
            // spliting each line with the |
            String[] parts = line.split("\\|");
            if (parts.length == 2) {
                users[userCount][0] = parts[0]; // id
                users[userCount][1] = parts[1]; // password
                // adding by 1
                userCount++;
            }
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(frame, "Error loading users: " + e.getMessage());
    }
    return userCount;
}

    // putting in users.txt
    public void saveUserToFile(String filename, String id, String password) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(id + "|" + password);
            writer.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error saving user: " + e.getMessage());
        }
    }
// check if there are same user id
    public boolean isDuplicateUser(String id) {
        for (int i = 0; i < userCount; i++) {
            if (users[i][0].equals(id)) {
                return true;
            }
        }
        return false;
    }

    // for books.txt read
    public void BooksFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("books.txt"))) {
            String line;
            int index = 0;
            while ((line = reader.readLine()) != null && index < books.length) {
                String[] parts = line.split("\\|");
                if (parts.length == 5) {
                    String title = parts[0];
                    String author = parts[1];
                    boolean checkedOut = Boolean.parseBoolean(parts[2]);
                    String borrower = parts[3];
                    String code = parts[4];
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

    // for books.txt
    public void saveBooksToFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Book book : books) {
                String line = book.getTitle() + "|" +
                              book.getAuthor() + "|" +
                              book.checkedout() + "|" +
                              book.getBorrower() + "|" +
                              book.getCode();
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error saving books: " + e.getMessage());
        }
    }



    public int getUserCount() {
        return userCount;
    }
}

