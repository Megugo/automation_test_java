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
public class ItemsItem{

	@JsonProperty("product")
	private Product product;

	@JsonProperty("quantity")
	private int quantity;

	@JsonProperty("_id")
	private String id;
}