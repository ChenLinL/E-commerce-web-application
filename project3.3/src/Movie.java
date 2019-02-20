import java.util.ArrayList;

public class Movie {
	private String movie_Id;
	private String title;
	private String releaseYear;
	private String director;
	private String actor;
	private ArrayList<String> genre;
	
	public Movie(){
		
	}
	
	public Movie(String movie_id, String title, String ReleaseYear, String director, String actor, ArrayList<String> genre) {
		this.movie_Id = movie_id;
		this.title = title;
		this.releaseYear = ReleaseYear;
		this.director = director;
		this.actor = actor;
		this.genre = genre;
	}
	
	public ArrayList<String> getGenre()
	{
		return genre;
	}
	
	public void setGenre(ArrayList<String> genre)
	{
		this.genre = genre;
	}
	
	public String getActor()
	{
		return actor;
	}
	
	public void setActor(String actor)
	{
		this.actor = actor;
	}
	
	public String getId()
	{
		return movie_Id;
	}
	
	public void setId(String movie_id)
	{
		this.movie_Id = movie_id;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	public String getReleaseYear() {
		return releaseYear;
	}

	public void setYear(String ReleaseYear) {
		this.releaseYear = ReleaseYear;
	}

	public String getDirector() {
		return director;
	}

	public void setName(String director) {
		this.director = director;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(getId());
		sb.append(", ");
		sb.append(getTitle());
		sb.append(", ");
		sb.append(getReleaseYear());
		sb.append(", ");
		sb.append(getDirector());
		sb.append(", " + getActor());
		sb.append(", " + getGenre());
		
		return sb.toString();
	}
}
