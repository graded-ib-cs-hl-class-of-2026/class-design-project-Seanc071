import java.io.*; // file operations
import java.time.LocalDateTime; // get time rn
import java.time.format.DateTimeFormatter; // time format
import java.util.*;
import javax.swing.*; // GUI popups

// https://docs.oracle.com/javase/8/docs/api/java/io/package-summary.html

public class LibraryFile {
    private JFrame frame; // the tab used for showing error
    private Book[] books = new Book[5]; // array to hold book
    private String[][] users = new String[100][2]; // 2d array to hold user id and password
    private int userCount = 0; // number of users stored
    private String registeredID; // currently logged-in user
    private String[] requestedBooks = new String[100]; // stores up to 100 book requests
    private int requestedCount = 0; // how many requested books there are
    private boolean onMainMenu = false; // tracks if user is on main menu (for Alt+M)
    private LibraryGUI gui; // reference to GUI

    /** this is a constructor initializing the class with the data below */
    public LibraryFile(JFrame frame) {
        this.frame = frame; // initializes the LibraryFile class with the JFrame from the GUI for popup handling



    }

    public void setGui(LibraryGUI gui) {
        this.gui = gui;
        // connects the LibraryFile with the GUI instance
    }

    // Basically syncing the data
    public void setOnMainMenu(boolean state) {
        onMainMenu = state;
        // sets the onMainMenu status to true or false
    }

    public boolean isOnMainMenu() {
        return onMainMenu;
    }

    // sets the current logged in user ID
    public void setRegisteredID(String registeredID) {
        this.registeredID = registeredID;
    }

    /** loads users and books from the file */
    public void loadData() {
        loadUsers();
        loadBooks();
    }

    /** handle login functionality */
    public void handleLogin(String id, String pw) {
        if (id.isEmpty() || pw.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter your ID and Password");
            return;
            // checks for empty input
        }
        for (int i = 0; i < userCount; i++) {
            if (users[i][0].equals(id) && users[i][1].equals(pw)) {
               // loops through the users to check for a match
                registeredID = id;
                logSession("logged in");
                gui.showMainMenu();
                return;
                // if login is successful, log the session and show the main menu
            }
        }
        JOptionPane.showMessageDialog(frame, "Wrong ID or password");
    }// if login fails, show an error popup

    /** handle register functionality */
    public void handleRegister(String id, String pw, Runnable callback) {
        // handles user registration
        if (id.isEmpty() || pw.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please fill out both box");
            return;
            // checks if ID and password are empty
        }
        if (userExists(id)) {
            JOptionPane.showMessageDialog(frame, "This ID already exists. Try a different one.");
            return;
            // checks if the ID already exists
        }
        users[userCount][0] = id;
        users[userCount][1] = pw;
        userCount++;
        // adds the new user to the array
        saveUser(id, pw);
        registeredID = id;
        JOptionPane.showMessageDialog(frame, "Registered successfully!");
        callback.run();
        // saves user to the file, sets logged-in user, and runs the callback
    }

    // check if there are same user id
    private boolean userExists(String id) {
        for (int i = 0; i < userCount; i++) {
            if (users[i][0].equals(id)) return true;
        }
        return false;
    }

    /** loading existing from the file users.txt */
    private void loadUsers() {
        // reads users.txt
        try (BufferedReader reader = new BufferedReader(new FileReader("src/users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] col = line.split("\\|");
                if (col.length == 2) {
                    users[userCount][0] = col[0];
                    users[userCount][1] = col[1];
                    userCount++;
                }
            }
        } catch (IOException e) {
            // reads line by line, splits ID and password, and stores them
            JOptionPane.showMessageDialog(frame, "Error loading users: " + e.getMessage());
        }
    }

    // putting in users.txt
    private void saveUser(String id, String password) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/users.txt", true))) {
            writer.write(id + "|" + password);
            writer.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error saving user: " + e.getMessage());
        }
    }

    // for books.txt read
    private void loadBooks() {
        // reads book information from books.txt
        try (BufferedReader reader = new BufferedReader(new FileReader("src/books.txt"))) {
            String line;
            int index = 0;
            while ((line = reader.readLine()) != null && index < books.length) {
                String[] col = line.split("\\|");
                if (col.length == 5) {
                    books[index++] = new Book(col[0], col[1], Boolean.parseBoolean(col[2]), col[3], col[4]);
                }
            }
        } catch (IOException e) {
            // each line contains: Title|Author|CheckedOut|Borrower|Code
            JOptionPane.showMessageDialog(frame, "Error loading books: " + e.getMessage());
        }
    }

    // for books.txt to save the data
    private void saveBooks() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/books.txt"))) {
            for (Book book : books) {
                if (book != null) {
                    // loops through the books
                    writer.write(String.join("|",
                            book.getTitle(), book.getAuthor(), String.valueOf(book.checkedout()),
                            book.getBorrower(), book.getCode()));
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error saving books: " + e.getMessage());
        }
    }

    /** logs login/logout activity to logins.txt */
    public void logSession(String action) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/logins.txt", true))) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            writer.write("[" + timestamp + "] " + registeredID + " " + action);
            writer.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error logging session: " + e.getMessage());
        }
    }

    /** for borrowTime.txt */
    private void logBorrow(String action, Book book) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/borrowTime.txt", true))) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            writer.write(timestamp + ", " + registeredID + ", " + action + ", " + book.getTitle() + ", " + book.getCode());
            writer.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error logging borrow: " + e.getMessage());
        }
    }

    /** write current book catalog to catalog.txt */
    public void CatalogFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/catalog.txt"))) {
            for (Book book : books) {
                String status = book.checkedout() ? "Checked out by: " + book.getBorrower() : "Available";
                String line = String.format("Title: %s | Author: %s | Code: %s | %s",
                        book.getTitle(), book.getAuthor(), book.getCode(), status);
                writer.write(line);
                writer.newLine();
            }
            JOptionPane.showMessageDialog(frame, "Catalog has been wrote in catalog.txt");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error exporting catalog: " + e.getMessage());
        }
    }

    // checks whether a book can be checked in or out
    // searches for a book by code
    // handles both check in and check out
    public String handleCheckInOut(String action, String code) {
        for (Book book : books) {
            if (book.getCode().equals(code)) {
                if (action.equals("Check Out")) {
                    if (!book.checkedout()) {
                        book.checkOut(registeredID);
                        saveBooks();
                        logBorrow("checked out", book);
                        return "Successfully checked out! Return in 2 weeks!";
                    } else {
                        return "Book already checked out.";
                    }
                } else if (action.equals("Check In")) {
                    if (book.checkedout() && book.getBorrower().equals(registeredID)) {
                        book.checkIn();
                        saveBooks();
                        logBorrow("checked in", book);
                        return "Successfully checked in! Thank you!";
                    } else {
                        return "You didn't borrow this book.";
                    }
                }
            }
        }
        return "Wrong code. Book not found.";
    }

    public String handleRequest(String title) {
        if (title.isEmpty()) return "Please enter a valid book title.";
        for (int i = 0; i < requestedCount; i++) {
            if (requestedBooks[i].equalsIgnoreCase(title)) return "Already requested.";
        }
        if (requestedCount < requestedBooks.length) {
            requestedBooks[requestedCount++] = title;
            return "Requested: " + title;
        } else {
            return "Request list is full.";
        }
    }

    public List<Book> searchBooks(String keyword) {
        List<Book> result = new ArrayList<>();
        for (Book book : books) {
            if (book != null && book.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(book);
            }
        }
        return result;
    }

    public String[] getRequestedBooks() {
        return Arrays.copyOf(requestedBooks, requestedCount);
    }

   public void clearRequests() {
    requestedBooks = new String[100];
    requestedCount = 0;
}


}
