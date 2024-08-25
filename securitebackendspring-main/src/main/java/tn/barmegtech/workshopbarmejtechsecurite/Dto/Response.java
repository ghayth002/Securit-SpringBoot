package tn.barmegtech.workshopbarmejtechsecurite.Dto;


import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
//@Getter
//@Setter
@Data
@Builder
public class Response {
   private String responseMessage;
   private String email;
}

