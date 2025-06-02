import java.awt.FlowLayout; // gap between the texts
import java.awt.Font; // font
import java.awt.GridLayout; // how the column and row is organized
import java.awt.KeyEventDispatcher; // explained below
import java.awt.KeyboardFocusManager; // explained below
import java.awt.event.KeyEvent; // explained below
import java.util.List; // Used JButton class to create a push button on the UI
import javax.swing.JButton; // Used to render a read-only text label or images on the UI
import javax.swing.JFrame; // used for creating dialog boxes for displaying a message, confirm box, or input dialog box
import javax.swing.JLabel; // used for the password, and we can edit them
import javax.swing.JOptionPane;
import javax.swing.JPanel; // renders an editable single-line text box
import javax.swing.JPasswordField; // The Font class represents fonts, which are used to render text in a visible way
import javax.swing.JTextField;

/**
What I used to learn GUI and build this code:
https://runestone.academy/ns/books/published/csawesome/Unit6-Arrays/toctree.html
https://www.youtube.com/watch?v=Kmgo00avvEw&t=4523s
https://stackoverflow.com/questions/9347076/how-to-remove-all-components-from-a-jframe-in-java/32253547
*/

/** Class for LibraryGUI */
public class LibraryGUI {
    private JFrame frame; // creates GUI for the error pop ups
    private LibraryFile libraryFile; // the controller

    public LibraryGUI(LibraryFile libraryFile, JFrame frame) {
        this.libraryFile = libraryFile;
        this.frame = frame;

        frame.setSize(500, 500); // setting the frame size

        // first, goes through the login page
        Login();
        // https://chatgpt.com/share/6807a1d8-7c14-8007-af9d-f9faf2270159
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
        if (k.isAltDown() && k.getKeyCode() == KeyEvent.VK_M && libraryFile.isOnMainMenu()) {
            showManagerPage(); 
            return true;
            // this return true make the typing(alt + m) doesn't go to anywhere else(key blocked)
        }
        return false;// key works normally
    }
});
        frame.setVisible(true);
    }

    /** Login page */
    private void Login() {
        libraryFile.setOnMainMenu(false); //  for the manager page
        frame.getContentPane().removeAll(); // remove all the factors of things in the last page
        frame.setLayout(new GridLayout(5, 1));

        JLabel title = new JLabel("SC's LIBRARY", JLabel.CENTER);
        title.setFont(new Font("DialogInput", Font.BOLD, 35));
        title.setForeground(new java.awt.Color(0, 102, 204)); // blue color

        JTextField id = new JTextField();
        JPasswordField passWord = new JPasswordField();
        JButton login = new JButton("Login");
        JButton register = new JButton("Register");

        login.setBackground(new java.awt.Color(0, 153, 255));
        login.setForeground(java.awt.Color.WHITE);
        login.setFont(new Font("DialogInput", Font.BOLD, 16));
        register.setBackground(new java.awt.Color(0, 204, 102));
        register.setForeground(java.awt.Color.WHITE);
        register.setFont(new Font("DialogInput", Font.BOLD, 16));

        frame.getRootPane().setDefaultButton(login);

        JPanel a = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0)); // adding some buttons
        a.add(login);
        a.add(register);

        frame.add(title);
        frame.add(id);
        frame.add(passWord);
        frame.add(a);

        // if press button...
        login.addActionListener(click -> {   
            String ID = id.getText();
            String PassWord = new String(passWord.getPassword());
            libraryFile.handleLogin(ID, PassWord);
        });

        register.addActionListener(click -> RegisterPage());
        frame.revalidate();
        frame.repaint();
    }
// basically same thing below here
    /** Register Page */
    private void RegisterPage() {
        libraryFile.setOnMainMenu(false);
        frame.getContentPane().removeAll();
        frame.setLayout(new GridLayout(5, 1));
        JLabel title = new JLabel("Register New Account", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 25));

        JTextField newid = new JTextField();
        JPasswordField newpassWord = new JPasswordField();
        JButton confirm = new JButton("Confirm");
        JButton back = new JButton("Back");

        confirm.addActionListener(click -> {
            String newId = newid.getText();
            String newPass = new String(newpassWord.getPassword());
            libraryFile.handleRegister(newId, newPass, () -> Login());
        });

        back.addActionListener(click -> Login());
        frame.getRootPane().setDefaultButton(confirm);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.add(confirm);
        buttonPanel.add(back);

        frame.add(title);
        frame.add(newid);
        frame.add(newpassWord);
        frame.add(buttonPanel);
        frame.revalidate();
        frame.repaint();
    }

    /** Main menu */
    public void showMainMenu() {
        MainMenu();
    }

    private void MainMenu() {
        libraryFile.setOnMainMenu(true);
        frame.getContentPane().removeAll();
        frame.setTitle("Main Menu");
        frame.setLayout(new GridLayout(6, 1));

        JButton checkout = new JButton("Check Out Book");
        JButton checkin = new JButton("Check In Book");
        JButton request = new JButton("Request Book");
        JButton find = new JButton("Book Finder");
        JButton exit = new JButton("Exit");

        checkout.addActionListener(click -> checkInOutPage("Check Out"));
        checkin.addActionListener(click -> checkInOutPage("Check In"));
        request.addActionListener(click -> requestPage());
        find.addActionListener(click -> finderPage());
        exit.addActionListener(click -> Login());

        frame.add(new JLabel("Main Menu", JLabel.CENTER));
        frame.add(checkout);
        frame.add(checkin);
        frame.add(find);
        frame.add(request);
        frame.add(exit);

        frame.revalidate();
        frame.repaint();
    }

    /** Page for checking in/out */
    private void checkInOutPage(String action) {
        frame.getContentPane().removeAll();
        frame.setLayout(new GridLayout(5, 1));

        JTextField code = new JTextField();
        JLabel result = new JLabel("", JLabel.CENTER);
        JButton submit = new JButton("Submit");
        JButton back = new JButton("Main Page");

        submit.addActionListener(click -> {
            String res = libraryFile.handleCheckInOut(action, code.getText());
            result.setText(res);
        });

        back.addActionListener(click -> MainMenu());
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panel.add(submit);
        panel.add(back);

        frame.getRootPane().setDefaultButton(submit);

        frame.add(new JLabel(action + " Book", JLabel.CENTER));
        frame.add(code);
        frame.add(result);
        frame.add(panel);
        frame.revalidate();
        frame.repaint();
    }

    /** Request book page */
    private void requestPage() {
        frame.getContentPane().removeAll();
        frame.setLayout(new GridLayout(5, 1));

        JTextField title = new JTextField();
        JLabel result = new JLabel("", JLabel.CENTER);
        JButton request = new JButton("Request");
        JButton back = new JButton("Main Page");

        request.addActionListener(click -> {
            String res = libraryFile.handleRequest(title.getText());
            result.setText(res);
        });

        back.addActionListener(click -> MainMenu());
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panel.add(request);
        panel.add(back);

        frame.getRootPane().setDefaultButton(request);

        frame.add(new JLabel("Request Book", JLabel.CENTER));
        frame.add(title);
        frame.add(result);
        frame.add(panel);
        frame.revalidate();
        frame.repaint();
    }

    /** Finder page */
    private void finderPage() {
        frame.getContentPane().removeAll();
        frame.setLayout(new GridLayout(5, 1));

        JTextField keyword = new JTextField();
        JLabel result = new JLabel("Results:", JLabel.CENTER);
        JPanel results = new JPanel(new GridLayout(0, 1));
        JButton search = new JButton("Search");
        JButton back = new JButton("Main Page");

        search.addActionListener(click -> {
            results.removeAll();
            List<Book> found = libraryFile.searchBooks(keyword.getText());
            if (found.isEmpty()) {
                results.add(new JLabel("No matched books found.", JLabel.CENTER));
            } else {
                for (Book book : found) {
                    results.add(new JLabel(book.toString(), JLabel.CENTER));
                }
            }
            frame.revalidate();
            frame.repaint();
        });

        back.addActionListener(click -> MainMenu());
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panel.add(search);
        panel.add(back);

        frame.add(new JLabel("Book Finder", JLabel.CENTER));
        frame.add(keyword);
        frame.add(result);
        frame.add(panel);
        frame.add(results);
        frame.revalidate();
        frame.repaint();
    }
   private void showManagerPage() {
    frame.getContentPane().removeAll();
    frame.setLayout(new GridLayout(0, 1));

    frame.add(new JLabel("Requested Books", JLabel.CENTER));

    String[] requests = libraryFile.getRequestedBooks();
    if (requests.length == 0) {
        frame.add(new JLabel("No books requested.", JLabel.CENTER));
    } else {
        for (String req : requests) {
            frame.add(new JLabel(req, JLabel.CENTER));
        }
    }

    JButton clear = new JButton("Clear Requests");
    JButton export = new JButton("Export Catalog");
    JButton back = new JButton("Main Menu");

    clear.addActionListener(e -> {
        libraryFile.clearRequests();
        JOptionPane.showMessageDialog(frame, "All requests cleared.");
        showManagerPage();
    });

    export.addActionListener(e -> {
        libraryFile.CatalogFile();
        JOptionPane.showMessageDialog(frame, "Catalog exported.");
    });

    back.addActionListener(e -> MainMenu());

    frame.add(clear);
    frame.add(export);
    frame.add(back);

    frame.revalidate();
    frame.repaint();
}} 