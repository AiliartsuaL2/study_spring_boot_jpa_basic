package hellojpa;

import javax.persistence.*;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");// persistence.xml 에 설정한 unitName

        EntityManager em = emf.createEntityManager(); // 데이터베이스 커넥션을 하나 받아온 것

        EntityTransaction tx = em.getTransaction(); // 트랜잭션을 받아옴
        tx.begin(); // 트랜잭션 시작

        try{
//            Member member = new Member();
//            member.setId(2L);
//            member.setName("luvsole");

            Member member = em.find(Member.class, 1L);// find 메서드 파라미터 1 : 도메인 타입, 2: id
            member.setName("astronut"); // persist없이도 Collection처럼 update 처리가 된다. >> JPA가 관리해줌

            List<Member> resultList = em.createQuery("select m from Member as m ", Member.class)
                    .setFirstResult(1)//번부터 limit
                    .setMaxResults(10)//개수 가져오기 offset,,
                    .getResultList();

            for (Member member1 : resultList) {
                System.out.println("member1 = " + member1.getName());
            }


            //em.persist(member);
            tx.commit();
        }
        catch (Exception e){
            tx.rollback();
        }finally {
            em.close(); // 엔티티 매니저가 DB 커넥션을 물고있어서 꼭 닫아야 함
        }
        emf.close();

    }
}
