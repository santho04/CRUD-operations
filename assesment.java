import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class assesment {

    private static final String API_TOKEN = "M1XbCrQnV47mHjZcYnpsUBOFPvDFo/PJhrSaPRNlJb4MYO0gWTQamNFIw6zn7KLCo5e4xx7aTm5dbDhSxFBJS64Qd34M8gn0/78uGxilK4Rg4MVNLVO62u18ElX59BSJJ1Pcfyar2N5TqrQMEypEOQ==";
    private static final String BASE_URL = "https://apihub.document360.io/v2/Drive/Folders";
    private static final String USER_ID = "223e32e4-7a9f-4a69-ba9a-5f201c00dbda";


    @SuppressWarnings("ConvertToTryWithResources")
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        // Task 1: GET all folders
        getAllFolders();

        // Task 2: POST create folder
        System.out.print("\nEnter a name for the new folder: ");
        String folderName = scanner.nextLine();
        String createdFolderId = createFolder(folderName);

        // Task 3: PUT update folder
        System.out.print("\nEnter a new name for the folder: ");
        String updatedName = scanner.nextLine();
        updateFolderName(createdFolderId, updatedName);

        // Task 4: DELETE folder
        System.out.print("\nDo you want to delete the folder? (yes/no): ");
        if (scanner.nextLine().equalsIgnoreCase("yes")) {
            deleteFolder(createdFolderId);
        }

        scanner.close();
    }

    // Task 1: GET
    private static void getAllFolders() throws IOException {
        HttpURLConnection conn = setupConnection(BASE_URL, "GET");
        System.out.println("\n[GET] Fetching all folders...");
        printRequestDetails(conn, null);
        printResponse(conn);
    }

    // Task 2: POST
    private static String createFolder(String folderName) throws IOException {
        HttpURLConnection conn = setupConnection(BASE_URL, "POST");
        conn.setDoOutput(true);
        String body = "{\"title\": \"" + folderName + "\", \"user_id\": \"" + USER_ID + "\"}";

        try (OutputStream os = conn.getOutputStream()) {
            os.write(body.getBytes());
            os.flush();
        }

        System.out.println("\n[POST] Creating new folder...");
        printRequestDetails(conn, body);
        String response = getResponse(conn);
        System.out.println(response);

        // Extract folderId from response (very basic logic; adjust as needed)
        String folderId = extractValueFromNestedData(response, "media_folder_id");
        System.out.println("Folder ID: " + folderId);
        return folderId;
    }

    // Task 3: PUT
    // Task 3: PUT
    private static void updateFolderName(String folderId, String newName) throws IOException {
        if (folderId == null || folderId.isEmpty()) {
            System.out.println("Invalid folder ID. Cannot proceed with PUT.");
            return;
        }

        String endpoint = BASE_URL + "/" + folderId;
        HttpURLConnection conn = setupConnection(endpoint, "PUT");
        conn.setDoOutput(true);

        // This header is sometimes required for update actions to work.
        conn.setRequestProperty("If-Match", "*");

        String body = "{\"title\": \"" + newName + "\"}";

        try (OutputStream os = conn.getOutputStream()) {
            os.write(body.getBytes());
            os.flush();
        }

        System.out.println("\n[PUT] Updating folder name...");
        printRequestDetails(conn, body);
        printResponse(conn);
    }


    // Task 4: DELETE
    private static void deleteFolder(String folderId) throws IOException {
        String endpoint = BASE_URL + "/" + folderId;
        HttpURLConnection conn = setupConnection(endpoint, "DELETE");

        System.out.println("\n[DELETE] Deleting folder...");
        printRequestDetails(conn, null);
        printResponse(conn);
    }

    // Common setup
    @SuppressWarnings("deprecation")
    private static HttpURLConnection setupConnection(String endpoint, String method) throws IOException {
    URL url = new URL(endpoint);
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestMethod(method);
    conn.setRequestProperty("Content-Type", "application/json");
    conn.setRequestProperty("api_token", API_TOKEN);
    conn.setRequestProperty("user_id", USER_ID);
    return conn;
}

    // Print request details
    private static void printRequestDetails(HttpURLConnection conn, String body) {
        System.out.println("URL: " + conn.getURL());
        System.out.println("Method: " + conn.getRequestMethod());
        System.out.println("Headers: api_token=" + API_TOKEN);
        if (body != null) {
            System.out.println("Body: " + body);
        }
    }

    // Print response
    private static void printResponse(HttpURLConnection conn) throws IOException {
        System.out.println("Status: " + conn.getResponseCode());
        System.out.println(getResponse(conn));
    }

    // Read response string
    private static String getResponse(HttpURLConnection conn) throws IOException {
    InputStream stream;

    if (conn.getResponseCode() < 400) {
        stream = conn.getInputStream();
    } else {
        stream = conn.getErrorStream(); // ← if 4xx or 5xx
        if (stream == null) {
            return "No error stream available. Response code: " + conn.getResponseCode();
        }
    }

    BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
    StringBuilder response = new StringBuilder();
    String line;
    while ((line = reader.readLine()) != null) {
        response.append(line).append("\n");
    }
    reader.close();
    return response.toString();
}

    private static String extractValueFromNestedData(String json, String key) {
    String searchKey = "\"" + key + "\":\"";
    int start = json.indexOf("\"data\":");
    if (start == -1) return null;

    int keyStart = json.indexOf(searchKey, start);
    if (keyStart == -1) return null;

    keyStart += searchKey.length();
    int end = json.indexOf("\"", keyStart);
    return json.substring(keyStart, end);
}

}
