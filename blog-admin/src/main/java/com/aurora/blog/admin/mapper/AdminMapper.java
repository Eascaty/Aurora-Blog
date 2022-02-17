package com.aurora.blog.admin.mapper;

import com.aurora.blog.admin.pojo.Admin;
import com.aurora.blog.admin.pojo.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminMapper extends BaseMapper<Admin> {

    List<Permission> findPermissionsByAdminId(Long adminId);


}
