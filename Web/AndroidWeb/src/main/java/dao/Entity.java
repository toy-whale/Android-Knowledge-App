package dao;

public class Entity {
	
	private String name;
    private String subject;
    private String description;
    private String property;
    private String relative;
    private String question;
    private String image;
    
    public Entity(String name, String subject, String description, String property, String relative, String question, String image) {
        this.name = name;
        this.subject = subject;
        this.description = description;
        this.property = property;
        this.relative = relative;
        this.question = question;
        this.image = image;
    }

    public String getName() {
    	if (name == null || name.equals(""))
    		return "null";
        return name;
    }

    public String getSubject() {
    	if (subject == null || subject.equals(""))
    		return "null";
        return subject;
    }

    public String getDescription() {
    	if (description == null || description.equals(""))
    		return "null";
        return description;
    }

    public String getProperty() {
    	if (property == null || property.equals(""))
    		return "null";
        return property;
    }

    public String getRelative() {
    	if (relative == null || relative.equals(""))
    		return "null";
        return relative;
    }

    public String getQuestion() {
    	if (question == null || question.equals(""))
    		return "null";
        return question;
    }
    
    public String getImage() {
    	if (image == null || image.equals(""))
    		return "null";
        return image;
    }
    
}
