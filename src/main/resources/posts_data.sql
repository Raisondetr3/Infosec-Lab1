INSERT INTO users (username, password) VALUES
                                           ('testuser', '$2a$10$/VBpws5W5OqKAK0kxvQd4..UNGv.qNb7L1RVCOgCQ8PgIMyNAj9qi');

INSERT INTO posts (title, content, author_id) VALUES
                                                  ('Welcome Post', 'Welcome to our secure API!', 1),
                                                  ('Security Guide', 'Important security practices for developers.', 1),
                                                  ('Quick Tip', 'Always validate user input.', 1),
                                                  ('Unicode Test éñ中文', 'Testing Unicode characters support.', 1),
                                                  ('Long Content Post', 'This is a longer post with more detailed content for testing purposes. It contains multiple sentences and demonstrates how the API handles larger text blocks.', 2),
                                                  ('Admin News', 'Latest updates from administration.', 1),
                                                  ('User Feedback', 'Thank you for using our API!', 1),
                                                  ('Tech Update', 'New features have been deployed.', 1),
                                                  ('Safety First', 'Security is our top priority.', 1),
                                                  ('Final Test', 'Last post for testing purposes.', 1);