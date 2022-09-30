package uos.selab.dtos;

import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor()
@NoArgsConstructor()
@Setter
@Getter
@Builder
public class UpdateItemDTO {

    @Size(max = 45, message = "title의 최대 크기는 45입니다.")
    private String title;

    @Size(max = 300, message = "description의 최대 크기는 300입니다.")
    private String description;

}
