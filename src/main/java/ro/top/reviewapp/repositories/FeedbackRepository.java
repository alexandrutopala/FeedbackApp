package ro.top.reviewapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ro.top.reviewapp.entities.Feedback;

public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
}
