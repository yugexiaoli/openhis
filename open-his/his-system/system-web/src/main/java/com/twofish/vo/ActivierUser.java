package com.twofish.vo;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import com.twofish.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户角色权限装载类
 * @author ccy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivierUser implements Serializable {

	private User user;
	private List<String> roles= Collections.EMPTY_LIST;//角色
	private List<String> permissions=Collections.EMPTY_LIST;//权限


}
