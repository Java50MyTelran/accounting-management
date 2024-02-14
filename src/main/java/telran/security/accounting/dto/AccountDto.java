package telran.security.accounting.dto;
import jakarta.validation.constraints.*;
public record AccountDto(@NotEmpty @Email String email, @NotNull @Size(min=8)String password,
		@NotEmpty String[] roles ) {

}
