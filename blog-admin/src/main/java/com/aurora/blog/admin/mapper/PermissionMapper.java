package com.aurora.blog.admin.mapper;

import com.aurora.blog.admin.pojo.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PermissionMapper extends BaseMapper<Permission> {

    @Select("SELECT * FROM ms_permission where id in (select permission_id from ms_admin_permission where admin_id = #{adminId}")
    List<Permission> findPermissionsByAdminId(Long adminId);

}
