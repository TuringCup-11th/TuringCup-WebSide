package com.turingcup.csa.Services.impl;

import com.turingcup.csa.Entities.TeamInfoEntity;
import com.turingcup.csa.Repository.TeamInfoRepository;
import com.turingcup.csa.Services.SignUpTeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;

@Service
public class SignUpTeamServiceImpl implements SignUpTeamService {
    private TeamInfoEntity teamInfo;
    private TeamInfoRepository teamInfoRepository;

    @Autowired
    public SignUpTeamServiceImpl(TeamInfoEntity teamInfo, TeamInfoRepository teamInfoRepository){
        this.teamInfo = teamInfo;
        this.teamInfoRepository = teamInfoRepository;
    }
    @Override
    public boolean checkTeamName(String teamName) {
        return teamInfoRepository.checkContainData("teamName", teamName);
    }

    @Override
    public boolean singUpTeamInfo(HttpServletRequest request) {
        //后台过滤规则
        //TODO 完善后台过滤
        if(request.getParameter("teamName") == null || request.getParameter("teamLeaderName") == null ||
            request.getParameter("teamLeaderCollege") == null || request.getParameter("QQ") == null ||
            request.getParameter("telNumber") == null || request.getParameter("EMailAddress") == null){
            return false;
        }
        //写入队伍数据至实体
        teamInfo.setTeamName(request.getParameter("teamName"));
        teamInfo.setTeamLeaderName(request.getParameter("teamLeaderName"));
        teamInfo.setTeamLeaderCollege(request.getParameter("teamLeaderCollege"));
        teamInfo.setQq(request.getParameter("QQ"));
        teamInfo.setTelNumber(request.getParameter("telNumber"));
        teamInfo.setEmailAddress(request.getParameter("EMailAddress"));
        if(request.getParameter("teamMemberOneName") != null){
            teamInfo.setTeamMember1Name(request.getParameter("teamMemberOneName"));
            teamInfo.setTeamMember1College(request.getParameter("teamMemberOneCollege"));
        }
        if(request.getParameter("teamMemberTwoName") != null){
            teamInfo.setTeamMember2Name(request.getParameter("teamMemberTwoName"));
            teamInfo.setTeamMember2College(request.getParameter("teamMemberTwoCollege"));
        }
        teamInfo.setSignupTime(new Timestamp(System.currentTimeMillis()));
        //写入数据库，并查找teamID
        int teamID = teamInfoRepository.writeTeamInfo(teamInfo);
        if(teamID != -1){
            request.setAttribute("teamID", teamID);
            return true;
        }
        return false;

    }
}
