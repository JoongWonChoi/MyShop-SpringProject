package jwcbook.jwcshop.Service;


import jwcbook.jwcshop.Repository.MemberRepository;
import jwcbook.jwcshop.domain.Member;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
//기본적으로 데이터 변경은 트랜잭션이 꼭 있어야함.
//하지만 기능마다, 읽기 전용 트랜잭션이 더 많은 기능은 public 애노테이션을 @Transaction(readOnly=true)로 놓고, 그렇지 않은 메서드에는 따로 표기를 해주는 방법도 있음.

//
//4. @AllArgsConstructor
//5.
@RequiredArgsConstructor // ==> lombok, final 있는 필드만 갖고 생성자를 자동으로 만들어줌.
public class MemberService {

    private final MemberRepository memberRepository; //변경할 일이 없기 때문에 final로 지정

    /*//3. 생성자 의존관계 주입 ==> 가장 좋은 방식
    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }*/

    /*//1. 필드 의존관계 주입
    @Autowired
    private MemberRepository memberRepository;

    //2. setter메서드 의존관계 주입
    @Autowired
    public void setMemberRepository(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
*/
    //회원 가입
    @Transactional // 읽기 전용이 아니라 '쓰기'를 해야하는 기능이기에 readOnly=true로 설정하면 안됨.
    public Long join(Member member) {
        validateDuplicateMember(member); //중복 회원 검증 비즈니스 로직
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        //중복회원 존재하면 예외 발생 시키기
        List<Member> findMembers = memberRepository.findByName(member.getName()); //만약 동시에 같은 이름의 멤버가 들어온다면?
        if (findMembers.isEmpty()==false) { //이름으로 찾은 회원 리스트가 비어있지 않으면 . . ==> 이미 존재한다는 의미!
            throw new IllegalStateException("already exists . .");
        }
    }
    //회원 전체 목록
    //@Transactional(readOnly = true) :: 읽기 전용 트랜잭션!
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }
    //회원 한 명 조회 ==> 단건 조회
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }


    //transaction이 readonly로 되어있어서 수정 사항이 반영 되지 않았었음!!!
    @Transactional
    public void updateMember(Long id, String name, String city, String street, String zipcode) {
        memberRepository.changeMember(id, name, city, street, zipcode);
    }

    //회원 전체 조회


}
