public class Book {
    // set title, author, if it's checked out or not, who borrowed it, and the code
    private String title;
    private String author;
    private boolean checkedout;
    private String borrower;
    private String code;

    // constructor: called when creating a new book
    //initializes a new book object with the information i gave
    // it sets the initial values for all the book info
    public Book(String title, String author, boolean checkedout, String borrower, String code){
        this.title = title;
        this.author = author;
        this.checkedout = checkedout;
        this.borrower = borrower;
        this.code = code;
    

    }
    // return the book's title
    public String getTitle(){
        return title;
    }
    // return the author's name
    public String getAuthor(){
        return author;
    }
    // return if the book is currently checked out (true/false)
    public boolean checkedout(){
        return checkedout;

    }
    // return who borrowed the book
    public String getBorrower(){
        return borrower;
    }
    // if it's checked out then get the borrower name
    public void checkOut(String name){
        checkedout = true;
        borrower = name;
    }
    // if it's checked in
    public void checkIn(){
        checkedout = false;
    }
    // get the code
    public String getCode(){
        return code;
    }
    // return the book info in a readable format
    // this is used when displaying book info on the screen
    public String toString(){
        String overall;
        if(checkedout){
            overall = " /Check-out: "+ borrower;
    
        }else{
            overall = " /Check-out: Available";
        }
        return "Title: " + title + " /Author: " + author + " /Code: " + code + " " + overall;
    }
    
}
