package com.tmate.service;


import com.tmate.domain.MemberDTO;
import com.tmate.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberMapper memberMapper;

    @Override
    public void join(MemberDTO dto) {
        memberMapper.insert(dto);
    }
}
