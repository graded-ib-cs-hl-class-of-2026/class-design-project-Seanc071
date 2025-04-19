import java.awt.FlowLayout; // Used to read text from a file line by line efficiently
import java.awt.Font;// Used to write text to a file efficiently
import java.awt.GridLayout; // A class that reads raw character data from a file
import java.awt.KeyEventDispatcher; // A class that writes raw character data to a file
import java.awt.KeyboardFocusManager; // Signals that an I/O exception of some sort has occurred
import java.awt.event.KeyEvent;// represents date and time
import javax.swing.JButton;// format of the time
import javax.swing.JFrame; // gap between the texts
import javax.swing.JLabel; // font
import javax.swing.JOptionPane; // how the column and row is organzied
import javax.swing.JPanel; // explained below
import javax.swing.JPasswordField; // explained below
import javax.swing.JTextField; // explained below
// https://docs.oracle.com/javase/8/docs/api/java/awt/package-summary.html
// https://docs.oracle.com/javase/7/docs/api/java/awt/package-summary.html







/*
What I used to learn GUI and build this code:
https://runestone.academy/ns/books/published/csawesome/Unit6-Arrays/toctree.html
https://www.youtube.com/watch?v=Kmgo00avvEw&t=4523s 
https://stackoverflow.com/questions/9347076/how-to-remove-all-components-from-a-jframe-in-java/32253547
 


*/

/**Class for LibrarayGUI */
public class LibraryGUI {
    
    private String registeredID = null;
    private String registeredPassWord = null; //stores registered ID and password
    private Book[] books = new Book[5]; //array holds 5 books in the library
    private JFrame frame = new JFrame(); //creates GUI, https://docs.oracle.com/javase/tutorial/uiswing/components/frame.html
    private String[] requestedBooks = new String[100]; //it stores up to 100 book requests
    private int requestedCount = 0; //keeps track of how may have been requested
    private boolean isOnMainMenu = false; //I created a boolean(for the manager page) so that the manager page can only open in the main menu
     // 2d array that can store up to 100 user, and sotre 2 value(id and password)
    private String[][] users = new String[100][2];
     // keeps track of how many useres are currently stored in the useres array
    private int userCount = 0;
    // declared a variable to hold an object of the class LibraryFileHandler
    private LibraryFileHandler fileHandler;





    








    /**LibraryGUI */
    public LibraryGUI(){
        // callows the libraryfilehandler work with the library's data
        fileHandler = new LibraryFileHandler(frame, books, users, userCount, requestedBooks, requestedCount, registeredID);
        // updates userCout in the GUI to match how many useres were acutally loaded from the file
        fileHandler.loadUsersFromFile();
        userCount = fileHandler.loadUsersFromFile();  // reads the book data from books.txt and loads it into the books array
        fileHandler.BooksFile();

        frame.setSize(500, 500); // setting the frame size
       
       
       // first, goes through the login page
        Login();
        
// If the keyboard Alt + m is typed at the main menu
// KeyboardFocusManager is a class.
// getCurrentKeyboardFocusManager() is a static method that returns the current keyboard manager object.
// We store that object in variable 'a'.
// Now 'a' can be used to listen to all key events in the program.
/**So, KeyboardFocusManager a calls KeyboardFocusManager and becomes ready to check the keys typed below. */
KeyboardFocusManager a = KeyboardFocusManager.getCurrentKeyboardFocusManager();
//so I made that 'a' uses keyeventdispatcher which hleps to check every key first. If the pressed key is the thing I declared(Alt + m) it goes through this code first, but not, it just lets the key go normally
a.addKeyEventDispatcher(new KeyEventDispatcher() {
    //created boolean to check if alt + m is typed
            public boolean dispatchKeyEvent(KeyEvent k) {
                //if the alt and m key is pressed, and is in main meun, it goes to the manager page
                if (k.isAltDown() && k.getKeyCode() == KeyEvent.VK_M && isOnMainMenu) {
                    ManagerPage();
                    return true;// this return true make the typing(alt + m) doesn't go to anywhere else(key blocked)
                }
                return false;// key works normally
            }
        });

       
    }











/**Login page */
private void Login(){
    // for the key press thing, it is false so that the manager page won't work
     isOnMainMenu = false;
     // removes the original page's button, text..etc
     // this is the original page but for example if you go to the register page and come back, I don't want that register page text and button remain, so i delete it
    frame.getContentPane().removeAll();
    // tells that the frame use grid layout with 5 rows and 1 column
    frame.setLayout(new GridLayout(5,1));
    
    // creates a label text on screen saying SC's LIBRARY placing it center
    JLabel title = new JLabel("SC's LIBRARY", JLabel.CENTER);
    // set title font, bold,and size
    title.setFont(new Font("DialogInput",Font.BOLD,35));
    // set the color for title
    title.setForeground(new java.awt.Color(0, 102, 204)); // blue color
    //https://chatgpt.com/share/6802c383-62ec-8007-8a73-6152b3c3e856

    // creates text box for ID
    JTextField id = new JTextField();
    // creates text box for Password
    JPasswordField passWord = new JPasswordField();
    // creates button for asking Login
    JButton login = new JButton("Login");
    // creates button asking Register
    JButton register = new JButton("Register");
    
    // setting button's color, font and size
    login.setBackground(new java.awt.Color(0, 153, 255));    // light blue
    login.setForeground(java.awt.Color.WHITE);  
    login.setFont(new Font("DialogInput", Font.BOLD, 16));
    register.setBackground(new java.awt.Color(0, 204, 102)); // green
    register.setForeground(java.awt.Color.WHITE);
    register.setFont(new Font("DialogInput", Font.BOLD, 16));
    
    // It makes the login button get clicked automatically when the user presses enter on the keyboard
    frame.getRootPane().setDefaultButton(login);  
    
    // created a JPanel(mini box) and named a. the flowLayout shows where to place and the gap betwwen them
    JPanel a = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
    //panel i want to add
    a.add(login);
    a.add(register);

    // Added the thing i set above
    frame.add(title);
    frame.add(id);
    frame.add(passWord);
    frame.add(a);    

    // for login text box, if I click the box, 
    login.addActionListener(click -> {
        // make the user can text in the text box
        String ID = id.getText();
        // as JPasswordField use char[] i need in this form
        String PassWord = new String(passWord.getPassword());
        
        // if either ID or PassWord text box is empty, print that message
        if (ID.isEmpty() || PassWord.isEmpty()) {  
            JOptionPane.showMessageDialog(frame, "Please enter your ID and Password");
            return;
        }
   boolean found = false;
        for (int i = 0; i < userCount; i++) {
            if (users[i][0].equals(ID) && users[i][1].equals(PassWord)) {
                registeredID = ID;
                registeredPassWord = PassWord;
                // tells who is currently logged in by giving the file registeredID
                 fileHandler.setRegisteredID(registeredID); 
                found = true;
                break;
            }
        } if (found) {
            // this tells to write login thing in the logins.txt
        fileHandler.logSession("logged in");
            MainMenu();
        } else {
            JOptionPane.showMessageDialog(frame, "Wrong ID or password"); 
        }
    });
        
        // when clicked register, go to Register Page
    register.addActionListener(click -> RegisterPage());
    frame.setVisible(true);
}




















    /**Register Page */
    private void RegisterPage(){
    // so the manage page doesn't work
    isOnMainMenu = false;
    
    // removes the original page's button, text..etc
    frame.getContentPane().removeAll();
    // same way as explained above..
    frame.setLayout(new GridLayout(5, 1));
    JLabel title = new JLabel("Register New Account", JLabel.CENTER);
    title.setFont(new Font("Arial", Font.BOLD, 25));

    // 2 text box for password and id
    JTextField newid= new JTextField();
    JPasswordField newpassWord = new JPasswordField();
    // 2 buttons asking confirm and back
    JButton confirm = new JButton("Confirm");
    JButton back = new JButton("Back");

    // same method as login
    confirm.addActionListener(click -> {
        String newId = newid.getText();
        String newPass = new String(newpassWord.getPassword());











       if (!newId.isEmpty() && !newPass.isEmpty()) {
                // this checks if the ueser id is already taken
                if (fileHandler.isDuplicateUser(newId)) {
                    JOptionPane.showMessageDialog(frame, "This ID already exists. Try a different one.");
                    return;
                }
                // stores the new id and pssword in the useres[][] array
                users[userCount][0] = newId;
                users[userCount][1] = newPass;
                // increase user cout so the ntext user goes to next row
                userCount++;
                // updates the user count
                fileHandler.setUserCount(userCount); 
                // save user name with id and password in the users.txt
                fileHandler.saveUserToFile("users.txt", newId, newPass);
                registeredID = newId;
                registeredPassWord = newPass;
                 fileHandler.setRegisteredID(registeredID); // update the registered id in both gui and file 

                JOptionPane.showMessageDialog(frame, "Registered successfully!");
                Login();
            } else {
                JOptionPane.showMessageDialog(frame, "Please fill out both box");
            }
        });










    // if pressed back go to login page
    back.addActionListener(click -> Login());

    //https://forums.oracle.com/ords/apexds/post/how-to-have-a-default-button-for-a-jpanel-9050
    frame.getRootPane().setDefaultButton(confirm);  


    // create panel, setting its location
    JPanel button = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
    // panel i want to add
    button.add(confirm);
    button.add(back);
    
    // add in the tab that i set above
    frame.add(title);
    frame.add(newid);
    frame.add(newpassWord);
    frame.add(button);

    // tells the frame to check if the layout or componet have changed(changed in above)
    frame.revalidate();
    // tell the frame to repaint the tab
    frame.repaint();


}










    /**Main menu */
    private void MainMenu(){
        // the key alt + m works in here
        isOnMainMenu = true;
        // remove the past things in the tab
        frame.getContentPane().removeAll();
        // same method....
        frame.setTitle("Main Menu");                  
        frame.setLayout(new GridLayout(6,1));

        // create buttons in the tab that layout as above
        JButton checkout = new JButton("Check Out Book");
        JButton checkin = new JButton("Check In Book");
        JButton request = new JButton("Request Book");
        JButton find = new JButton("Book Finder");
        JButton exit = new JButton("Exit");

        // shows which page or which text it is going through when button clicked
        checkout.addActionListener(click -> checkinandout("Check Out"));
        checkin.addActionListener(click -> checkinandout("Check In"));
        request.addActionListener(click -> showRequestPage());
        find.addActionListener(click -> FinderPage());
        exit.addActionListener(click -> {
            // make the logins.txt to write logged out
            fileHandler.logSession("logged out");
            Login();
        });
        request.addActionListener(click -> showRequestPage());

        
        // add the title, and the things above
        frame.add(new JLabel("Main Menu", JLabel.CENTER));
        frame.add(checkout);
        frame.add(checkin);
        frame.add(find);
        frame.add(request);
        frame.add(exit);
        
        // same work as other method
        frame.revalidate();
        frame.repaint();
    }












    /**page for checking and checking out the books */
    private void checkinandout(String action){
        // remove original things
        frame.getContentPane().removeAll();
        frame.setLayout(new GridLayout(5,1));


        // add new button, label, text box
        JTextField codee = new JTextField();
        JButton submit = new JButton("Submit");
        JButton back = new JButton("Main Page");
        // result either if the book is checked in or out. the text is currently nothing but will be set
        JLabel result = new JLabel("", JLabel.CENTER);
        
        // when clicked submit
        submit.addActionListener(click ->{
            // put a textbox so the user can write the code
            String code = codee.getText();
            // currently the book isn't checked out or checked in
            boolean success = false;
            
            // loop through every book in the books array. checking if any book contains the code that user wrote
            // starts with [0] until the number of books, each time add by once
            for(int i = 0; i < books.length; i++){
                Book book = books[i];
                // if there is a code that matches the book code
                if(book.getCode().equals(code)){
                    // if the user clicked the check out button, and book wasn't checked out originally
                    if(action.equals("Check Out") && !book.checkedout()){
                        // if the book is checked it shows the borrower's id
                        book.checkOut(registeredID);
                        result.setText("Successfully checked out! Return in 2 weeks!");



                        // saves the updated list of books to the books.txt
                        fileHandler.saveBooksToFile("books.txt");
                        // in the borrowlog.txt say it is borrowed by someone
                        fileHandler.logBorrowAction("checked out", book);
                        

                        
                        // else if the user clicked check in button and book is checked out
                    }else if(action.equals("Check In") && book.checkedout()){
    if (book.getBorrower().equals(registeredID)) {
        book.checkIn();
        result.setText("Successfully checked in! Thank you!");
        //same thing as above
    fileHandler.saveBooksToFile("books.txt");
    fileHandler.logBorrowAction("checked in", book);

    } else {
        result.setText("You did not check out this book.");
    }
    }
    else if (action.equals("Check Out")) {
                        
                        result.setText("Book already checked out.");
                    } else {
                        result.setText("Book already checked in");
                    }
                    // if any of this process goes through, set success as true
                    success = true;
                    break;
                }
            }
            // if success is false it means the code is wrong
            if(!success){
                result.setText("Wrong code. Book not found.");
            }
        }); 
        // if clicked back button
        back.addActionListener(click -> MainMenu());

        // add buttons and other features that are set above
        JPanel button = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        button.add(submit);
        button.add(back);

        frame.getRootPane().setDefaultButton(submit);  


        
        frame.add(new JLabel(action + " Book", JLabel.CENTER));
        frame.add(codee);
        frame.add(result);
        frame.add(button);
   
        // same way as above...
        frame.revalidate();
        frame.repaint();
    }


    /**page for the request books */
    private void showRequestPage(){
        // same way as above..
        frame.getContentPane().removeAll();
        frame.setLayout(new GridLayout(5,1));

        JTextField titlee= new JTextField();
        JButton request = new JButton("Request");
        JButton back = new JButton("Main Page");
        JLabel result = new JLabel("", JLabel.CENTER);

        // when pressed request button
        request.addActionListener(click -> {
        // get the text from titlee text box
        String title = titlee.getText();
        // if no word typed
    if (title.isEmpty()) {
        result.setText("Please enter a valid book title.");
        return;
    }

    // for loop if the requested book is already requested
    for (int i = 0; i < requestedCount; i++) {
        if (requestedBooks[i].equalsIgnoreCase(title)) {
            result.setText("Already requested.");
            return;
        }
    } 
    // if there is a space for request books
    if (requestedCount < requestedBooks.length) {
        requestedBooks[requestedCount++] = title;
        result.setText("Requested: " + title);
        // if no space
    } else {
        result.setText("Request list is full.");
    }
});
    // when clikced back
        back.addActionListener(click -> MainMenu());

        // create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.add(request);
        buttonPanel.add(back);

        frame.getRootPane().setDefaultButton(request);  


        frame.add(new JLabel("Request Book", JLabel.CENTER));
        frame.add(titlee);
        frame.add(result);
        frame.add(buttonPanel);

        frame.revalidate();
        frame.repaint();

    }










/**manager page(when press Alt + m in the main menu) */
private void ManagerPage() {
    frame.getContentPane().removeAll();
    // 0rows(adding one rows for each time the thing added) and stack it vertically(1 column)
    frame.setLayout(new GridLayout(0, 1)); 
    
    // same way as above 
    JLabel title = new JLabel("Requested Books", JLabel.CENTER);
    frame.add(title);

    // if there are no requested books
    if (requestedCount == 0) {
        frame.add(new JLabel("No books have been requested yet.", JLabel.CENTER));
    } 
    // if the books are listed, add new j label and add the titel of the books
    else {
        for (int i = 0; i < requestedCount; i++) {
            frame.add(new JLabel(requestedBooks[i], JLabel.CENTER));
        }
    }

        // when press clear button, the array for requested books become null(0) and write All requests cleared
    JButton clear = new JButton("Clear All Requests");
    clear.addActionListener(click -> {
        for (int i = 0; i < requestedCount; i++) {
            requestedBooks[i] = null;
        }
        requestedCount = 0;
        JOptionPane.showMessageDialog(frame, "All requests cleared.");
        // after to refresh the page, reload the manager page
       ManagerPage(); 
    });
// same thing
    JButton export = new JButton("Export Book Catalog");
    export.addActionListener(click -> fileHandler.CatalogFile());
    
    JButton back = new JButton("Back to Main Menu");
    back.addActionListener(click -> MainMenu());

    frame.add(clear); // Add clear button
    frame.add(back);  // Add back button
    frame.add(export);

// same thing
    frame.revalidate();
    frame.repaint();
}
    









    /**finder page */
    private void FinderPage() {
        // same/similar thing
        frame.getContentPane().removeAll();
    

        JTextField searching = new JTextField();
        JButton search = new JButton("Search");
        // result for the searched books. the text is currently nothing but will be set
        JLabel result = new JLabel("", JLabel.CENTER);
        JButton back = new JButton("Main Page");
    
    // 0rows(adding one rows for each time the thing added) and stack it vertically(1 column)
        JPanel results = new JPanel(new GridLayout(0, 1)); 
    
        // when search is clicked
        search.addActionListener(click -> {
            results.removeAll(); 

            // set the text to lower case for easeir matching
            String keyword = searching.getText().toLowerCase();
         
            
            if (keyword.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter a book title to search.");
;
            } 
           
            // loop through the books to see if any title matches the keyword
            for (int i = 0; i < books.length; i++) {
                Book book = books[i];
                // get a book title in lower case and check if it contains the keyword user wrote
                // if it contains 
                if (book.getTitle().toLowerCase().contains(keyword)) {
                    results.add(new JLabel(book.toString(), JLabel.CENTER));
                   
                }
            
            } 
            // if no components that matched found
            if (results.getComponentCount() == 0) {
                results.add(new JLabel("No matched books found.", JLabel.CENTER));
            }
                   
            frame.getRootPane().setDefaultButton(search);  

        
            result.setText("Results:");
            frame.revalidate();
            frame.repaint();
        });
        
        // when pressed back
        back.addActionListener(click -> MainMenu());

        // add the buttons 
        JPanel button = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        button.add(search);
        button.add(back);
         
        // add the things listed above
        frame.add(new JLabel("Book Finder", JLabel.CENTER));
        frame.add(searching);
        frame.add(result);
        frame.add(button);
        frame.add(results); 
        frame.revalidate();
        frame.repaint();
    }
}    
