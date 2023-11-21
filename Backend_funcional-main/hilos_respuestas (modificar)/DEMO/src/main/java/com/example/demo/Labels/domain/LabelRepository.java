package com.example.demo.Labels.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LabelRepository extends JpaRepository<Label,Long> {
    Label findByValor(String valor);

}
