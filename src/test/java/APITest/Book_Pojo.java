package APITest;

public class Book_Pojo {

    private String author;

    private String id;

    private String title;

    public String getAuthor ()
    {
        return author;
    }

    public void setAuthor (String author)
    {
        this.author = author;
    }

    public String getId ()
    {
        return id;
    }

    // Read-only so NO need a setter for ID
    /*public void setId (String id)
    {
        this.id = id;
    }*/

    public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [author = "+author+", id = "+id+", title = "+title+"]";
    }

}
