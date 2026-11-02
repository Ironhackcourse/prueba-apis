import axios from 'axios';

export async function handleLogin(req, res) {
    const { username, password } = req.body;

    try {
        const response = await axios.post('http://localhost:8080/api/v1/auth/login', {
            username, 
            password
        });

        if (response.status === 200) {
            res.cookie('authToken', response.data.token, { httpOnly: true, secure: true });
            res.redirect('/dashboard');
        } else {
            res.status(401).json({ error: 'Invalid credentials' });
        }
    } catch (error) {
        res.status(500).json({ error: 'Server error' });
    }
}
