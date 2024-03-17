package com.example.signproject.controller;

import com.example.signproject.Utils.FileUtil;
import com.example.signproject.Utils.JwtUtil;
import com.example.signproject.Utils.ResultJson;
import com.example.signproject.ViewModel.JoinTeamViewModel;
import com.example.signproject.entity.Team;
import com.example.signproject.entity.Team_Other;
import com.example.signproject.entity.Team_User;
import com.example.signproject.entity.User;
import com.example.signproject.service.*;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("team")
public class TeamController {
    @Resource
    private TeamService teamService;
    @Resource
    private Team_UserService team_userService;
    @Resource
    private Team_OtherService team_otherService;
    @Resource
    private UserService userService;
    @Resource
    private Competition_TeamService competition_teamService;


    @PostMapping("/create")
    public ResultJson<Object> TeamCreate(@RequestBody Team team) {
        return this.teamService.create(team);
    }

    @GetMapping("/all")
    public ResultJson<Object> AllTeam(String leader) {
        return this.teamService.findByLeader(leader);
    }

    @DeleteMapping("/delete")
    public ResultJson<Object> DeleteTeam(@RequestParam("id") Serializable id) {
        return this.teamService.delete(id);
    }

    @PostMapping("/join")
    public ResultJson<Object> JoinTeam(@RequestBody JoinTeamViewModel joinTeamViewModel) {
        return team_userService.JoinTeam(joinTeamViewModel);
    }

    @GetMapping("/getteam")
    public ResultJson<Object> GetTeam(@RequestHeader("token") String token) {
        //String token = request.getHeader("token");
        String id = JwtUtil.getId(token);
        return this.team_userService.GetTeamByUserId(id);
    }

    @DeleteMapping("/quitteam")
    public ResultJson<Object> QuitTeam(@RequestParam("team") int team, @RequestHeader("token") String token) {
        //String token = request.getHeader("token");
        String id = JwtUtil.getId(token);
        return this.team_userService.QuitTeamByUserId(id, team);
    }

    @GetMapping("/getuser")
    public ResultJson<Object> GetUser(@RequestParam("team") int team) {
        return this.team_userService.GetUserByTeamId(team);
    }

    @DeleteMapping("/deleteuser")
    public ResultJson<Object> DeleteUser(@RequestParam("team") int team, @RequestParam("id") String user) {
        return this.team_userService.DeleteUser(team, user);
    }

    @PostMapping("/addOther")
    public ResultJson<Object> AddOther(@RequestBody Team_Other team_other) {
        return this.team_otherService.add(team_other);
    }

    @DeleteMapping("/deleteOther")
    public ResultJson<Object> DeleteOther(@RequestParam("team") int team, @RequestParam("phone") String phone) {
        return this.team_otherService.delete(team, phone);
    }

    @GetMapping("/showOther")
    public ResultJson<Object> ShowOther(@RequestParam("team") int team) {
        return this.team_otherService.showAll(team);
    }

    @GetMapping("/download")
    public void download(HttpServletResponse response, @RequestParam("team") int team) throws IOException {
        Team t = this.teamService.getById(team);
        List<Team_User> Team_UserList = this.team_userService.getAll(team);
        List<User> userList = new ArrayList<>();
        for (Team_User teamUser : Team_UserList) {
            userList.add(this.userService.getById(teamUser.getUser()));
        }

        Workbook workbook = FileUtil.setExcel(userList);

        // 设置响应头信息，告诉浏览器这是一个需要下载的文件
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", String.format("attachment; filename=%s.xlsx", t.getName()));

        // 将Excel文件写入响应输出流
        workbook.write(response.getOutputStream());

        // 关闭工作簿
        workbook.close();
    }

    @GetMapping("/getCompetitionsByTeam")
    public ResultJson<Object> getCompetitionsByTeam(@RequestParam("team") int team) {
        List<String> list = this.competition_teamService.getAllCompetition(team);
        return list == null ? ResultJson.error("查询失败") : ResultJson.success(list);
    }
}
