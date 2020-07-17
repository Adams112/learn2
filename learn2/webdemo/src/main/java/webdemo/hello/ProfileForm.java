package webdemo.hello;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


public class ProfileForm {

	@Size(min = 2)
	private String twitterHandle;
	
	@Email
	@NotEmpty
	private String email;
	
	@NotNull
	private Date birthDate;
	
	private List<String> tastes;

	public String getTwitterHandle() {
		return twitterHandle;
	}

	public void setTwitterHandle(String twitterHandle) {
		this.twitterHandle = twitterHandle;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public List<String> getTastes() {
		return tastes;
	}

	public void setTastes(List<String> tastes) {
		this.tastes = tastes;
	}

	@Override
	public String toString() {
		return "ProfileForm [twitterHandle=" + twitterHandle + ", email=" + email + ", birthDate=" + birthDate
				+ ", tastes=" + tastes + "]";
	}

}
