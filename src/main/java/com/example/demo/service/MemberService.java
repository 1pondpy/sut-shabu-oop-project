package com.example.demo.service;

import com.example.demo.entity.Member;
import com.example.demo.repository.MemberRepository;

public class MemberService {
    
    private MemberRepository memberRepository = new MemberRepository();
    public Member searchByPhoneNumber(String phoneNumber) {
        return memberRepository.findByPhoneNumber(phoneNumber);
    }
    public java.util.List<Member> getAllMembers() {
        return memberRepository.findAll();
    }
}
