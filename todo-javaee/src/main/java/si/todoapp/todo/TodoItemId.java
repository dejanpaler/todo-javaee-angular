package si.todoapp.todo;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.UUID;

/**
 * {@code TodoItemId} generates unique identifier based on {@code UDDI} as a String object.
 *
 */
public class TodoItemId {

    private static final int UUID_BYTE_LENGTH = 16;

    private TodoItemId(){
    }

    /**
     * Returns unique identifier using the URL and Filename safe character set and it omits padding characters.
     *
     * @return Identifier t
     */
    public static String generate() {
        UUID uuid = UUID.randomUUID();

        ByteBuffer uuidByteBuffer = ByteBuffer.wrap(new byte[UUID_BYTE_LENGTH]);
        uuidByteBuffer.putLong(uuid.getMostSignificantBits());
        uuidByteBuffer.putLong(uuid.getLeastSignificantBits());

        return Base64.getUrlEncoder().withoutPadding().encodeToString(uuidByteBuffer.array());
    }

}
