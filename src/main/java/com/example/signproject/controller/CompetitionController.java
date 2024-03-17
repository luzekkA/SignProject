package com.example.signproject.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.signproject.Enum.CategoryEnum;
import com.example.signproject.Enum.RoutinesOptionalEnum;
import com.example.signproject.Utils.FileUtil;
import com.example.signproject.Utils.JwtUtil;
import com.example.signproject.Utils.ResultJson;
import com.example.signproject.ViewModel.exportSheetViewModel;
import com.example.signproject.ViewModel.setActionScoresViewModel;
import com.example.signproject.entity.*;
import com.example.signproject.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("competition")
public class CompetitionController {
    @Resource
    private CompetitionService competitionService;
    @Resource
    private Competition_TeamService competition_teamService;
    @Resource
    private User_Team_CompetitionService user_team_competitionService;
    @Resource
    private Team_UserService team_userService;
    @Resource
    private UserService userService;
    @Resource
    private Action_CodeService action_codeService;
    @Resource
    private TeamService teamService;


    @PostMapping("/create")
    public ResultJson<Object> Create(@RequestBody Competition competition) {
        return competitionService.CreateCompetition(competition);
    }

    @GetMapping("/selectAll")
    public ResultJson<Object> SelectAll() {
        return competitionService.SelectAll();
    }

    @PostMapping("/join")
    public ResultJson<Object> Join(@RequestBody Competition_Team competition_Team) {
        return competition_teamService.JoinCompetition(competition_Team);
    }

    //用户退出队伍
    @DeleteMapping("/quit")
    public ResultJson<Object> Quit(@RequestParam("competition") String competition, @RequestParam("team") int team) {
        Competition_Team competition_Team = new Competition_Team();
        competition_Team.setCompetition(competition);
        competition_Team.setTeam(team);
        return competition_teamService.QuitCompetition(competition_Team);
    }

    @PostMapping("/addScore")
    public ResultJson<Object> AddScore(@RequestBody User_Team_Competition user_team_competition) {
        return user_team_competitionService.addScore(user_team_competition);
    }

    @GetMapping("/getScore")
    public ResultJson<Object> GetScore(@RequestHeader("token") String token) {
        String id = JwtUtil.getId(token);
        return user_team_competitionService.getScore(id);
    }

    @PostMapping("/info")
    public ResultJson<Object> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("competition") String competition) {
        return this.competitionService.UploadCompetitionInfo(competition, file);
    }

    @GetMapping("/getCategory")
    public ResultJson<Object> getCategory() {
        return ResultJson.success(CategoryEnum.values());
    }

    @GetMapping("/download")
    public void download(HttpServletResponse response, @RequestParam("competition") String competition) throws IOException {
        List<Competition_Team> teamList = this.competition_teamService.getAllTeam(competition);
        List<Team_User> Team_UserList = new ArrayList<>();
        List<User> userList = new ArrayList<>();
        for (Competition_Team competitionTeam : teamList) {
            Team_UserList.addAll(this.team_userService.getAll(competitionTeam.getTeam()));
        }
        for (Team_User teamUser : Team_UserList) {
            userList.add(this.userService.getById(teamUser.getUser()));
        }
        Workbook workbook = FileUtil.setExcel(userList);
        // 设置响应头信息，告诉浏览器这是一个需要下载的文件
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", String.format("attachment; filename=%s.xlsx", competition));
        // 将Excel文件写入响应输出流
        workbook.write(response.getOutputStream());
        // 关闭工作簿
        workbook.close();
    }

    //已使用硬编码实现
    @GetMapping("/getRoutineOptional")
    public ResultJson<Object> getRoutineOptional() {
        return ResultJson.success(RoutinesOptionalEnum.values());
    }

    //填写比赛小项
    @PostMapping("/update")
    public ResultJson<Object> chooseType(@RequestHeader("token") String token, @RequestBody User_Team_Competition user_team_competition) {
        String id = JwtUtil.getId(token);
        user_team_competition.setUser(id);
        return user_team_competitionService.set_TeamAndType(user_team_competition);
    }
    //未使用
    @GetMapping("/findCode")
    public ResultJson<Object> findCode(@RequestParam("code") String code, @RequestParam("type") String type) {
        return this.action_codeService.findCode(code, type);
    }
    //未使用
    @GetMapping("/exportSheet")
    public void exportSheet(HttpServletResponse response,@RequestParam("competition") String competition) throws IOException {
        List<User_Team_Competition> user_team_competitionList = user_team_competitionService.exportSheet(competition);
        List<exportSheetViewModel> userList = new ArrayList<>();

        int i = 1;
        for (User_Team_Competition user_team_competition : user_team_competitionList){
            exportSheetViewModel exportSheet = new exportSheetViewModel();
            exportSheet.setId(i++);
            exportSheet.setUserName(userService.getOne(Wrappers.<User>lambdaQuery()
                    .eq(User::getId,user_team_competition.getUser())).getUsername());
            exportSheet.setSort(user_team_competition.getSort());
            exportSheet.setTeam(teamService.getOne(Wrappers.<Team>lambdaQuery()
                    .eq(Team::getId,user_team_competition.getTeam())).getName());
            exportSheet.setUserid(user_team_competition.getUser());
            exportSheet.setType(user_team_competition.getType().getDisplay());
            exportSheet.setGroup(user_team_competition.getGroupp());
            exportSheet.setGender(user_team_competition.getUser().charAt(17) % 2 == 0 ? "男" : "女");
            userList.add(exportSheet);
        }
        Workbook workbook = FileUtil.setExcel_t(userList);

        // 设置响应头信息，告诉浏览器这是一个需要下载的文件
        response.setContentType("application/vnd.ms-excel");

        String filename = "抽签检录表.xlsx";
        String encodedFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFilename);

        //response.setHeader("Content-Disposition", "attachment; filename=抽签检录表.xlsx");

        // 将Excel文件写入响应输出流
        workbook.write(response.getOutputStream());

        // 关闭工作簿
        workbook.close();
        //return user_team_competitionService.exportSheet(competition);
    }

    //保存表格数据
    @PostMapping("/setActions_scores")
    public ResultJson<Object> setActions_scores(@RequestHeader("token") String token,@RequestBody setActionScoresViewModel setActionScoresViewModel) {
        String id = JwtUtil.getId(token);
        User_Team_Competition user_team_competition = user_team_competitionService.getOne(Wrappers.<User_Team_Competition>lambdaQuery()
                .eq(User_Team_Competition::getUser,id)
                .eq(User_Team_Competition::getCompetition,setActionScoresViewModel.getCompetition())
                .eq(User_Team_Competition::getTeam,setActionScoresViewModel.getTeam()));
        if (user_team_competition == null) {
            user_team_competition = new User_Team_Competition(id,
                    setActionScoresViewModel.getTeam(),
                    setActionScoresViewModel.getCompetition(),
                    0,null,null,0,null,null,false);
            user_team_competitionService.save(user_team_competition);
        }
        user_team_competition.setActions_scores(setActionScoresViewModel.getActions_scoresAsString());
        //user_team_competition.setUser(id);
        return user_team_competitionService.setActions_scores(user_team_competition);
    }
    //获取表格数据
    @GetMapping("/getActions_scores")
    public ResultJson<Object> getActions_scores(@RequestHeader("token") String token,@RequestParam("competition") String competition,@RequestParam("team") int team) {
        String id = JwtUtil.getId(token);
        return user_team_competitionService.getActions_scores(id,competition,team);
    }
    //判断是否填写表格 只能判断是否
    @GetMapping("/hasFill")
    public ResultJson<Object> hasFill(@RequestHeader("token") String token,@RequestParam("competition") String competition) {
        String id = JwtUtil.getId(token);
        return user_team_competitionService.getOne(Wrappers.<User_Team_Competition>lambdaQuery()
                .eq(User_Team_Competition::getUser,id)
                .eq(User_Team_Competition::getCompetition,competition)) ==null?ResultJson.success("未填写"):ResultJson.success("已填写");
    }


    @PostMapping("/uploadPayment")
    public ResultJson<Object> uploadPayment(@RequestParam("file") MultipartFile file, @RequestHeader("token") String token,@RequestParam("competition") String competition) {
        String id = JwtUtil.getId(token);
        User_Team_Competition user_team_competition = null;
        user_team_competition = user_team_competitionService.getOne(Wrappers.<User_Team_Competition>lambdaQuery()
                .eq(User_Team_Competition::getUser,id)
                .eq(User_Team_Competition::getCompetition,competition));
        if (FileUtil.isImage(file).getCode() == 20000 && user_team_competition != null) {
            //MultipartFile upload = file;
            String filename = user_team_competition.getTeam()
                    + user_team_competition.getTeam()
                    + Objects.requireNonNull(file
                            .getOriginalFilename())
                    .substring(file.getOriginalFilename().lastIndexOf("."));
            ResultJson<Object> resultJson = FileUtil.uploadFileToMinio(file, filename);
            if (resultJson.getCode() == 20000) {
//                user.setIco(resultJson.getData().toString());
                user_team_competition.setPayment(filename);
            } else {
                return resultJson;
            }
            if (user_team_competitionService.save(user_team_competition)) {
                return ResultJson.success();
            }
            return ResultJson.error("数据库信息更新失败");
        }
        return ResultJson.error("文件为空");
    }

    @GetMapping("/getPayment")
    public ResultJson<Object> getPayment(@RequestParam("competition") String competition) {
        return user_team_competitionService.getPayments(competition);
    }

    @GetMapping("/sureIsPay")
    public ResultJson<Object> sureIsPayment(@RequestParam("competition") String competition,@RequestParam("team") int team,@RequestParam("user") String user) {
        return user_team_competitionService.sureIsPay(competition,user,team);
    }
    //获取用户填写过的所有报表
    @GetMapping("AllActions_scores")
    public ResultJson<Object> AllActions_scores(@RequestParam("user") String user,@RequestParam("team") int team) {
        return user_team_competitionService.getAllActions_scores(user,team);
    }
}
