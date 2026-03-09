package com.dekk.category.domain.model;

import com.dekk.category.domain.exception.CategoryBusinessException;
import com.dekk.category.domain.exception.CategoryErrorCode;
import com.dekk.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.SQLDelete;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE categories SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Filter(name = "deletedFilter")
public class Category extends BaseTimeEntity {

    private static final int MAX_NAME_LENGTH = 10;
    private static final int PARENT_DEPTH = 0;
    private static final int CHILD_DEPTH = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> children = new ArrayList<>();

    @Column(nullable = false, length = MAX_NAME_LENGTH)
    private String name;

    @Column(nullable = false)
    private int depth;

    private Category(Category parent, String name, int depth) {
        this.parent = parent;
        this.name = name;
        this.depth = depth;
    }

    public static Category createParent(String name) {
        validateName(name);
        return new Category(null, name, PARENT_DEPTH);
    }

    public static Category createChild(Category parent, String name) {
        validateName(name);
        if (parent == null) {
            throw new CategoryBusinessException(CategoryErrorCode.PARENT_CATEGORY_REQUIRED);
        }
        if (parent.getDepth() != PARENT_DEPTH) {
            throw new CategoryBusinessException(CategoryErrorCode.ONLY_PARENT_CAN_HAVE_CHILDREN);
        }
        return new Category(parent, name, CHILD_DEPTH);
    }

    public void updateName(String name) {
        validateName(name);
        this.name = name;
    }

    public boolean isParent() {
        return this.depth == PARENT_DEPTH;
    }

    public boolean isChild() {
        return this.depth == CHILD_DEPTH;
    }

    private static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new CategoryBusinessException(CategoryErrorCode.CATEGORY_NAME_IS_REQUIRED);
        }
        if (name.length() > MAX_NAME_LENGTH) {
            throw new CategoryBusinessException(CategoryErrorCode.CATEGORY_NAME_TOO_LONG);
        }
    }
}
