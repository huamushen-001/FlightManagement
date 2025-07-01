package com.flightmanagement.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.flightmanagement.entity.CrewMember;

import java.util.List;

public interface CrewMemberService extends IService<CrewMember> {

    /**
     * 分页查询机组成员
     */
    IPage<CrewMember> getPage(Integer current, Integer size, String name, String position, String qualificationLevel);

    /**
     * 根据岗位查询机组成员
     */
    List<CrewMember> getByPosition(String position);

    /**
     * 根据资质等级查询机组成员
     */
    List<CrewMember> getByQualificationLevel(String qualificationLevel);

    /**
     * 添加机组成员
     */
    boolean addCrewMember(CrewMember crewMember);

    /**
     * 更新机组成员信息
     */
    boolean updateCrewMember(CrewMember crewMember);

    /**
     * 删除机组成员
     */
    boolean deleteCrewMember(Long id);

    /**
     * 更新飞行小时数
     */
    boolean updateFlightHours(Long id, Integer hours);
} 