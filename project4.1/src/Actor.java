public class Actor {
	private String name;
	private int birthYear;
	
	public Actor(){
		
	}
	
	public Actor(String name, int birthYear) {
		this.name = name;
		this.birthYear = birthYear;
	}
	
	public int getBirthYear() {
		return birthYear;
	}

	public void setAge(int birthYear) {
		this.birthYear = birthYear;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(getName());
		sb.append(", ");
		sb.append(getBirthYear());
		
		return sb.toString();
	}
}
