package com.flightmanagement.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.flightmanagement.common.Result;
import com.flightmanagement.entity.CrewMember;
import com.flightmanagement.service.CrewMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/crew-member")
@CrossOrigin
public class CrewMemberController {

    @Autowired
    private CrewMemberService crewMemberService;

    /**
     * 分页查询机组成员
     */
    @GetMapping("/page")
    public Result<IPage<CrewMember>> getPage(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String position,
            @RequestParam(required = false) String qualificationLevel) {
        
        IPage<CrewMember> page = crewMemberService.getPage(current, size, name, position, qualificationLevel);
        return Result.success(page);
    }

    /**
     * 根据岗位查询机组成员
     */
    @GetMapping("/position/{position}")
    public Result<List<CrewMember>> getByPosition(@PathVariable String position) {
        List<CrewMember> crewMembers = crewMemberService.getByPosition(position);
        return Result.success(crewMembers);
    }

    /**
     * 根据资质等级查询机组成员
     */
    @GetMapping("/qualification/{qualificationLevel}")
    public Result<List<CrewMember>> getByQualificationLevel(@PathVariable String qualificationLevel) {
        List<CrewMember> crewMembers = crewMemberService.getByQualificationLevel(qualificationLevel);
        return Result.success(crewMembers);
    }

    /**
     * 根据ID查询机组成员
     */
    @GetMapping("/{id}")
    public Result<CrewMember> getById(@PathVariable Long id) {
        CrewMember crewMember = crewMemberService.getById(id);
        return Result.success(crewMember);
    }

    /**
     * 添加机组成员
     */
    @PostMapping
    public Result<Boolean> add(@Valid @RequestBody CrewMember crewMember) {
        boolean success = crewMemberService.addCrewMember(crewMember);
        return success ? Result.success(true) : Result.error("添加失败");
    }

    /**
     * 更新机组成员信息
     */
    @PutMapping
    public Result<Boolean> update(@Valid @RequestBody CrewMember crewMember) {
        boolean success = crewMemberService.updateCrewMember(crewMember);
        return success ? Result.success(true) : Result.error("更新失败");
    }

    /**
     * 删除机组成员
     */
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        boolean success = crewMemberService.deleteCrewMember(id);
        return success ? Result.success(true) : Result.error("删除失败");
    }

    /**
     * 更新飞行小时数
     */
    @PutMapping("/{id}/flight-hours")
    public Result<Boolean> updateFlightHours(@PathVariable Long id, @RequestParam Integer hours) {
        boolean success = crewMemberService.updateFlightHours(id, hours);
        return success ? Result.success(true) : Result.error("更新失败");
    }
} 