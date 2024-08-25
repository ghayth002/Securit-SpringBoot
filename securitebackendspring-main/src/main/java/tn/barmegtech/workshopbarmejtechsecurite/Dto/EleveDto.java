package tn.barmegtech.workshopbarmejtechsecurite.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import tn.barmegtech.workshopbarmejtechsecurite.entites.Eleve;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class EleveDto extends RegisterRequeste{
	private String Diplome;
	public static Eleve Toentite(EleveDto eleveDto)
	{
		return Eleve.builder()
				.id(eleveDto.getId())
				.fullname(eleveDto.getFullname())
				.email(eleveDto.getEmail())
				.password(eleveDto.getPassword())
				.Diplome(eleveDto.getDiplome())	
				.build();
	}
	public static EleveDto fromentite(Eleve eleve)
	{
		return EleveDto.builder()
				.id(eleve.getId())
				.fullname(eleve.getFullname())
				.email(eleve.getEmail())
				.password(eleve.getPassword())
				.Diplome(eleve.getDiplome())	
				.build();
	}
	
}
