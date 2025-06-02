// https://docs.oracle.com/javase/8/docs/api/java/io/package-summary.html

/**
This is the main class that runs the Library GUI
*/
public class App {
    public static void main(String[] args) {
        javax.swing.JFrame frame = new javax.swing.JFrame(); // creates GUI window

        LibraryFile controller = new LibraryFile(frame); // pass frame to controller
        controller.loadData(); // loads users and books

        LibraryGUI gui = new LibraryGUI(controller, frame); // pass frame and controller to GUI
        controller.setGui(gui); // connect controller to GUI
    }
}