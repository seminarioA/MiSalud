package com.medx.beta.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PasswordValidatorTest {

    private PasswordValidator validator;

    @BeforeEach
    void setUp() {
        validator = new PasswordValidator();
        validator.initialize(null);
    }

    @Test
    void isValid_devuelveTrueParaPasswordCompleja() {
        boolean valido = validator.isValid("Abcd1234!", null);

        assertThat(valido).isTrue();
    }

    @Test
    void isValid_devuelveFalseSiFaltanRequisitos() {
        assertThat(validator.isValid("abcd1234", null)).isFalse();
        assertThat(validator.isValid("ABCD1234", null)).isFalse();
        assertThat(validator.isValid("Abcdefgh", null)).isFalse();
    }

    @Test
    void isValid_devuelveFalseCuandoEsNull() {
        assertThat(validator.isValid(null, null)).isFalse();
    }
}

