package ru.practicum.shareit.item.model.comment;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CommentMapper {


    public Comment toComment(CommentDto commentDto) {
        if (commentDto == null) {
            return null;
        }

        Comment.CommentBuilder comment = Comment.builder();

        comment.id(commentDto.getId());
        comment.text(commentDto.getText());
        comment.created(commentDto.getCreated());

        return comment.build();
    }


    public CommentDto toCommentDto(Comment comment) {
        if (comment == null) {
            return null;
        }

        CommentDto.CommentDtoBuilder commentDto = CommentDto.builder();

        commentDto.id(comment.getId());
        commentDto.authorName(comment.getAuthor().getName());
        commentDto.text(comment.getText());
        commentDto.created(comment.getCreated());

        return commentDto.build();
    }
}
