package com.wink.livemall.member.service.impl;

import com.wink.livemall.member.dao.LmMemberLinkDao;
import com.wink.livemall.member.dto.LmMemberLink;
import com.wink.livemall.member.service.LmMemberLinkService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;

@Service
public class LmMemberLinkServiceImpl implements LmMemberLinkService {

    @Resource
    private LmMemberLinkDao lmMemberLinkDao;


    @Override
    public void insert(LmMemberLink lmMemberLink) {
        lmMemberLinkDao.insertSelective(lmMemberLink);
    }

    @Override
    public List<LmMemberLink> findMemberIdOrLinkId(int memberId) {
        List<LmMemberLink> link =new LinkedList<>();
        List<LmMemberLink> member = lmMemberLinkDao.findMember(memberId);
        if(member!=null&&member.size()>0){
            for(LmMemberLink lmMemberLink:member){
                link.add(lmMemberLink);
            }
        }
        List<LmMemberLink> linkId = lmMemberLinkDao.findLinkId(memberId);
        if(linkId!=null&&linkId.size()>0){
            for(LmMemberLink lmMemberLink:linkId){
                lmMemberLink.setLink_id(lmMemberLink.getMember_id());
                link.add(lmMemberLink);
            }
        }
        return link;
    }

    @Override
    public void delMemberLink(int id) {
        lmMemberLinkDao.deleteByPrimaryKey(id);
    }

    @Override
    public List<LmMemberLink> findLink(int memberId, int linkId) {
        List<LmMemberLink> List =new LinkedList<>();
        List<LmMemberLink> link = lmMemberLinkDao.findLink(memberId, linkId);
        if(link!=null&&link.size()>0){
            for(LmMemberLink lmMemberLink:link){
                List.add(lmMemberLink);
            }
        }
        List<LmMemberLink> linkS = lmMemberLinkDao.findLink(linkId, memberId);
        if(linkS!=null&&linkS.size()>0){
            for(LmMemberLink lmMemberLink:linkS){
                List.add(lmMemberLink);
            }
        }
        return List;
    }

}
