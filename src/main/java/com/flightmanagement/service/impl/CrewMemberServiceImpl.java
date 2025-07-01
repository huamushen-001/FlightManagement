package com.flightmanagement.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.flightmanagement.entity.CrewMember;
import com.flightmanagement.mapper.CrewMemberMapper;
import com.flightmanagement.service.CrewMemberService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CrewMemberServiceImpl extends ServiceImpl<CrewMemberMapper, CrewMember> implements CrewMemberService {

    @Override
    public IPage<CrewMember> getPage(Integer current, Integer size, String name, String position, String qualificationLevel) {
        Page<CrewMember> page = new Page<>(current, size);
        return baseMapper.selectPage(page, name, position, qualificationLevel);
    }

    @Override
    public List<CrewMember> getByPosition(String position) {
        return baseMapper.selectByPosition(position);
    }

    @Override
    public List<CrewMember> getByQualificationLevel(String qualificationLevel) {
        return baseMapper.selectByQualificationLevel(qualificationLevel);
    }

    @Override
    public boolean addCrewMember(CrewMember crewMember) {
        crewMember.setCreateTime(LocalDateTime.now());
        crewMember.setUpdateTime(LocalDateTime.now());
        crewMember.setStatus("在职");
        crewMember.setCurrentFlightHours(0);
        return save(crewMember);
    }

    @Override
    public boolean updateCrewMember(CrewMember crewMember) {
        crewMember.setUpdateTime(LocalDateTime.now());
        return updateById(crewMember);
    }

    @Override
    public boolean deleteCrewMember(Long id) {
        return removeById(id);
    }

    @Override
    public boolean updateFlightHours(Long id, Integer hours) {
        CrewMember crewMember = getById(id);
        if (crewMember != null) {
            crewMember.setCurrentFlightHours(crewMember.getCurrentFlightHours() + hours);
            crewMember.setUpdateTime(LocalDateTime.now());
            return updateById(crewMember);
        }
        return false;
    }
} 