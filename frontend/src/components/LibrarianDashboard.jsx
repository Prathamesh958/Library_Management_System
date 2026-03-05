import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../api';

const LibrarianDashboard = () => {
    const [activeTab, setActiveTab] = useState('addBook');
    const navigate = useNavigate();

    const handleLogout = () => {
        localStorage.clear();
        navigate('/');
    };

    return (
        <div className="dashboard">
            <header>
                <div className="header-content">
                    <h1>Librarian Dashboard</h1>
                    <button onClick={handleLogout}>Logout</button>
                </div>
            </header>
            <nav className="tabs">
                <button onClick={() => setActiveTab('addCategory')} className={activeTab === 'addCategory' ? 'active' : ''}>Add Category</button>
                <button onClick={() => setActiveTab('addBook')} className={activeTab === 'addBook' ? 'active' : ''}>Add Book</button>
                <button onClick={() => setActiveTab('manageBooks')} className={activeTab === 'manageBooks' ? 'active' : ''}>Manage Books</button>
                <button onClick={() => setActiveTab('registerStudent')} className={activeTab === 'registerStudent' ? 'active' : ''}>Register Student</button>
                <button onClick={() => setActiveTab('issueBook')} className={activeTab === 'issueBook' ? 'active' : ''}>Issue Book</button>
                <button onClick={() => setActiveTab('returnBook')} className={activeTab === 'returnBook' ? 'active' : ''}>Return Book</button>
            </nav>
            <main className="content">
                {activeTab === 'addCategory' && <AddCategory />}
                {activeTab === 'addBook' && <AddBook />}
                {activeTab === 'manageBooks' && <ManageBooks />}
                {activeTab === 'registerStudent' && <RegisterStudent />}
                {activeTab === 'issueBook' && <IssueBook />}
                {activeTab === 'returnBook' && <ReturnBook />}
            </main>
        </div>
    );
};

const AddCategory = () => {
    const [name, setName] = useState('');
    const [message, setMessage] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            // Fix: Backend expects 'categoryName', not 'name'
            await api.post('/categories', { categoryName: name });
            setMessage('Category added successfully!');
            setName('');
        } catch (err) {
            setMessage('Failed to add category.');
        }
    };

    return (
        <div className="form-container">
            <h3>Add New Category</h3>
            <form onSubmit={handleSubmit}>
                <input placeholder="Category Name" value={name} onChange={e => setName(e.target.value)} required />
                <button type="submit">Add Category</button>
            </form>
            {message && <p>{message}</p>}
        </div>
    );
};

const AddBook = () => {
    const [categories, setCategories] = useState([]);
    const [book, setBook] = useState({ title: '', author: '', isbn: '', quantity: 1, categoryId: '' });
    const [message, setMessage] = useState('');

    const fetchCategories = async () => {
        try {
            const res = await api.get('/categories');
            setCategories(res.data);
            if (res.data.length === 0) {
                setMessage('No categories found. Please add a category first.');
            } else {
                setMessage('');
            }
        } catch (err) {
            console.error(err);
            setMessage('Failed to fetch categories.');
        }
    };

    useEffect(() => {
        fetchCategories();
    }, []);

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await api.post('/books', book);
            setMessage('Book added successfully!');
            setBook({ title: '', author: '', isbn: '', quantity: 1, categoryId: '' });
        } catch (err) {
            setMessage('Failed to add book.');
        }
    };

    return (
        <div className="form-container">
            <h3>Add New Book</h3>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '10px' }}>
                <p style={{ fontSize: '0.9rem', color: '#666', margin: 0 }}>Note: Add a category first if the list is empty.</p>
                <button type="button" onClick={fetchCategories} style={{ width: 'auto', fontSize: '0.8rem', padding: '5px 10px' }}>Refresh Categories</button>
            </div>

            <form onSubmit={handleSubmit}>
                <input placeholder="Title" value={book.title} onChange={e => setBook({ ...book, title: e.target.value })} required />
                <input placeholder="Author" value={book.author} onChange={e => setBook({ ...book, author: e.target.value })} required />
                <input placeholder="ISBN" value={book.isbn} onChange={e => setBook({ ...book, isbn: e.target.value })} required />
                <input type="number" placeholder="Quantity" value={book.quantity} onChange={e => setBook({ ...book, quantity: e.target.value })} required min="1" />
                <select value={book.categoryId} onChange={e => setBook({ ...book, categoryId: e.target.value })} required>
                    <option value="">Select Category</option>
                    {categories.map(cat => <option key={cat.categoryId} value={cat.categoryId}>{cat.categoryName}</option>)}
                </select>
                <button type="submit">Add Book</button>
            </form>
            {message && <p>{message}</p>}
        </div>
    );
};

const ManageBooks = () => {
    const [books, setBooks] = useState([]);
    const [searchTitle, setSearchTitle] = useState('');
    const [searchType, setSearchType] = useState('title');

    const handleSearch = async (e) => {
        e.preventDefault();
        try {
            let endpoint = '';
            if (searchType === 'title') {
                endpoint = `/books/search/title?title=${searchTitle}`;
            } else {
                endpoint = `/books/search/author?author=${searchTitle}`;
            }
            const res = await api.get(endpoint);
            setBooks(res.data);
        } catch (err) {
            console.error(err);
        }
    };

    const handleDelete = async (id) => {
        if (!window.confirm("Are you sure you want to delete this book?")) return;
        try {
            await api.delete(`/books/${id}`);
            setBooks(books.filter(b => b.bookId !== id && b.id !== id));
        } catch (err) {
            alert("Failed to delete book");
        }
    };

    return (
        <div className="form-container">
            <h3>Manage Books</h3>
            <form onSubmit={handleSearch} className="search-form" style={{ flexDirection: 'column', alignItems: 'flex-start' }}>
                <div style={{ marginBottom: '10px' }}>
                    <label style={{ marginRight: '15px' }}>
                        <input
                            type="radio"
                            name="searchTypeManage"
                            value="title"
                            checked={searchType === 'title'}
                            onChange={() => setSearchType('title')}
                            style={{ width: 'auto', marginBottom: 0 }}
                        /> Search by Title
                    </label>
                    <label>
                        <input
                            type="radio"
                            name="searchTypeManage"
                            value="author"
                            checked={searchType === 'author'}
                            onChange={() => setSearchType('author')}
                            style={{ width: 'auto', marginBottom: 0 }}
                        /> Search by Author
                    </label>
                </div>
                <div style={{ display: 'flex', gap: '10px', width: '100%' }}>
                    <input
                        placeholder={`Enter ${searchType === 'title' ? 'Book Title' : 'Author Name'}`}
                        value={searchTitle}
                        onChange={e => setSearchTitle(e.target.value)}
                    />
                    <button type="submit" style={{ width: 'auto' }}>Search</button>
                </div>
            </form>

            <div className="book-list">
                {books.map(book => (
                    <div key={book.id || book.bookId} className="book-card" style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                        <div>
                            <h4>{book.title}</h4>
                            <p>Author: {book.author}</p>
                        </div>
                        <button onClick={() => handleDelete(book.id || book.bookId)} style={{ width: 'auto', background: 'red' }}>Delete</button>
                    </div>
                ))}
            </div>
        </div>
    );
};

const RegisterStudent = () => {
    const [student, setStudent] = useState({ name: '', email: '', password: '', role: 'STUDENT', rollNo: '', course: '', year: 1 });
    const [message, setMessage] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await api.post('/users/register', student);
            setMessage('Student registered successfully!');
            setStudent({ name: '', email: '', password: '', role: 'STUDENT', rollNo: '', course: '', year: 1 });
        } catch (err) {
            setMessage('Failed to register student.');
            console.error(err);
        }
    };

    return (
        <div className="form-container">
            <h3>Register Student</h3>
            <form onSubmit={handleSubmit}>
                <input placeholder="Name" value={student.name} onChange={e => setStudent({ ...student, name: e.target.value })} required />
                <input placeholder="Email" type="email" value={student.email} onChange={e => setStudent({ ...student, email: e.target.value })} required />
                <input placeholder="Password" type="password" value={student.password} onChange={e => setStudent({ ...student, password: e.target.value })} required />
                <input placeholder="Roll No" value={student.rollNo} onChange={e => setStudent({ ...student, rollNo: e.target.value })} required />
                <input placeholder="Course" value={student.course} onChange={e => setStudent({ ...student, course: e.target.value })} required />
                <input placeholder="Year" type="number" value={student.year} onChange={e => setStudent({ ...student, year: e.target.value })} required min="1" />
                <button type="submit">Register</button>
            </form>
            {message && <p>{message}</p>}
        </div>
    );
};

const IssueBook = () => {
    const [bookId, setBookId] = useState('');
    const [studentId, setStudentId] = useState('');
    const [rollNo, setRollNo] = useState('');
    const [resolvedStudentId, setResolvedStudentId] = useState(null);
    const [message, setMessage] = useState('');

    const handleLookup = async () => {
        try {
            const res = await api.get(`/users/student/${rollNo}`);
            setResolvedStudentId(res.data.userId);
            setMessage(`Found Student: ${res.data.name}`);
        } catch (err) {
            setMessage('Student not found.');
            setResolvedStudentId(null);
        }
    };

    const handleIssue = async (e) => {
        e.preventDefault();
        try {
            await api.post(`/transactions/issue?bookId=${bookId}&studentId=${resolvedStudentId || studentId}`);
            setMessage('Book issued successfully!');
        } catch (err) {
            setMessage('Failed to issue book.');
        }
    };

    return (
        <div className="form-container">
            <h3>Issue Book</h3>
            <div className="lookup">
                <input placeholder="Enter Student Roll No" value={rollNo} onChange={e => setRollNo(e.target.value)} />
                <button type="button" onClick={handleLookup}>Find Student</button>
            </div>

            <form onSubmit={handleIssue}>
                <input placeholder="Book ID" value={bookId} onChange={e => setBookId(e.target.value)} required />
                <input placeholder="Student ID (Auto-filled)" value={resolvedStudentId || studentId} onChange={e => setStudentId(e.target.value)} required />
                <button type="submit">Issue Book</button>
            </form>
            {message && <p>{message}</p>}
        </div>
    );
};

const ReturnBook = () => {
    const [issueId, setIssueId] = useState('');
    const [rollNo, setRollNo] = useState('');
    const [history, setHistory] = useState([]);
    const [message, setMessage] = useState('');

    const handleLookup = async () => {
        try {
            // First get user ID from Roll No
            const userRes = await api.get(`/users/student/${rollNo}`);
            const userId = userRes.data.userId;

            // Then get history
            const res = await api.get(`/transactions/history/student/${userId}`);
            // Filter only issued books (not returned)
            setHistory(res.data.filter(t => t.status === 'ACTIVE'));
            setMessage(res.data.length === 0 ? 'No issued books found.' : '');
        } catch (err) {
            setMessage('Student or history not found.');
            setHistory([]);
        }
    };

    const handleReturn = async (id) => {
        try {
            await api.post(`/transactions/return/${id}`);
            setMessage('Book returned successfully!');
            setHistory(history.filter(h => h.issueId !== id));
        } catch (err) {
            setMessage('Failed to return book.');
        }
    };

    return (
        <div className="form-container">
            <h3>Return Book</h3>
            <div className="lookup">
                <input placeholder="Enter Student Roll No to find books" value={rollNo} onChange={e => setRollNo(e.target.value)} />
                <button type="button" onClick={handleLookup}>Find Issued Books</button>
            </div>
            {message && <p>{message}</p>}

            {history.length > 0 && (
                <table>
                    <thead>
                        <tr>
                            <th>Book</th>
                            <th>Issue Date</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        {history.map(record => (
                            <tr key={record.issueId}>
                                <td>{record.bookTitle}</td>
                                <td>{record.issueDate}</td>
                                <td>
                                    <button onClick={() => handleReturn(record.issueId)} style={{ width: 'auto', padding: '5px 10px' }}>Return</button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            )}

            <hr style={{ margin: '20px 0' }} />
            <p>Or return by Issue ID directly:</p>
            <div className="lookup">
                <input placeholder="Issue ID" value={issueId} onChange={e => setIssueId(e.target.value)} />
                <button type="button" onClick={() => handleReturn(issueId)}>Return</button>
            </div>
        </div>
    );
};

export default LibrarianDashboard;
