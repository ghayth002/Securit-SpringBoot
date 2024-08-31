package tn.barmegtech.workshopbarmejtechsecurite.Dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import tn.barmegtech.workshopbarmejtechsecurite.entites.Erole;
import tn.barmegtech.workshopbarmejtechsecurite.entites.User;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class RegisterRequeste {
	private Long id;
	private String fullname;
	private String email;
	private String password;
	private Erole role;
}
