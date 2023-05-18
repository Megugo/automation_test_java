package nexign_autotests.hw5.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddCartDto{

	@JsonProperty("product")
	private String product;

	@JsonProperty("quantity")
	private int quantity;

	@JsonProperty("user")
	private String user;
}