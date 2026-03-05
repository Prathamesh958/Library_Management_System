import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Login from './components/Login';
import LibrarianDashboard from './components/LibrarianDashboard';
import StudentDashboard from './components/StudentDashboard';
import './App.css';

function App() {
  const PrivateRoute = ({ children, role }) => {
    const token = localStorage.getItem('token');
    const userRole = localStorage.getItem('role');

    if (!token) return <Navigate to="/" />;
    if (role && role !== userRole) return <Navigate to="/" />;

    return children;
  };

  return (
    <Router>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route
          path="/librarian"
          element={
            <PrivateRoute role="LIBRARIAN">
              <LibrarianDashboard />
            </PrivateRoute>
          }
        />
        <Route
          path="/student"
          element={
            <PrivateRoute role="STUDENT">
              <StudentDashboard />
            </PrivateRoute>
          }
        />
      </Routes>
    </Router>
  );
}

export default App;
