package com.wolf.framework.layer.web.auth.model;

import com.wolf.common.ds.map.ObjectMap;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    private Long accountId;

    private String name;
    private String avatar;
    private String photo;
    private String introduction;
    private ObjectMap property = new ObjectMap();

    /**
     * current space | null
     */
    private Space space;
    /**
     * (optional) available space list for user
     * as DTO of the request after login
     */
    private List<Space> spaceList;


}
