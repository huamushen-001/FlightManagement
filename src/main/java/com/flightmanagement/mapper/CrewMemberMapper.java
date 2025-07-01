package com.flightmanagement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.flightmanagement.entity.CrewMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CrewMemberMapper extends BaseMapper<CrewMember> {

    /**
     * 分页查询机组成员
     */
    IPage<CrewMember> selectPage(Page<CrewMember> page, @Param("name") String name,
                                @Param("position") String position,
                                @Param("qualificationLevel") String qualificationLevel);

    /**
     * 根据岗位查询机组成员
     */
    List<CrewMember> selectByPosition(@Param("position") String position);

    /**
     * 根据资质等级查询机组成员
     */
    List<CrewMember> selectByQualificationLevel(@Param("qualificationLevel") String qualificationLevel);
} 