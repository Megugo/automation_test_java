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
public class DeleteFromCartDto{

	@JsonProperty("itemId")
	private String itemId;

	@JsonProperty("cartId")
	private String cartId;
}