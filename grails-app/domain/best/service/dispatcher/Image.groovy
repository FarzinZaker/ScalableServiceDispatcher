package best.service.dispatcher

class Image {

    String name
    byte[] content

    static mapping = {
        table 'image_files'
        content sqlType: 'blob'
    }
    static constraints = {
        content nullable: true
    }
}
