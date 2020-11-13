package com.wink.livemall.sys.adminrole.service;

import java.util.List;

public interface AdminRoleService {

    List<Integer> findRoleListByAdminId(int id);
}
