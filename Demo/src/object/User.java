package object;

public class User {
	private int id;
	private String name;
	private Student student;
	
	public User() {
	}
	public User(int id, String name) {
		this.id = id;
		this.name = name;
	}
	public User(int id, String name, Student s) {
		this.id = id;
		this.name = name;
		this.student=s;
	}
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Student getStudent() {
		return student;
	}
	public void setStudent(Student student) {
		this.student = student;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", student=" + student + "]";
	}
}
