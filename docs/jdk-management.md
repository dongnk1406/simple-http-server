# JDK Management on macOS (with jenv)

## 1. Find All Installed JDKs

JDKs installed via Homebrew may **not** be symlinked into `/Library/Java/JavaVirtualMachines`, so
`/usr/libexec/java_home -V` won't show them all. Use `find` to locate every installation:

```bash
find \
  /opt/homebrew/Cellar \
  /Library/Java/JavaVirtualMachines \
  /System/Library/Java/JavaVirtualMachines \
  -maxdepth 6 -name "release" 2>/dev/null \
  | xargs grep -l "JAVA_VERSION" 2>/dev/null
```

This prints the `release` file path for every JDK found. The JDK **home** is the parent directory of
each `release` file.

### Shortcut: Homebrew-only

```bash
ls /opt/homebrew/Cellar | grep openjdk
# then inspect each version, e.g.:
ls /opt/homebrew/Cellar/openjdk@17/
```

---

## 2. Get the JAVA_HOME Path for a Specific JDK

```bash
# Pattern: /opt/homebrew/Cellar/<formula>/<version>/libexec/openjdk.jdk/Contents/Home

# JDK 17 (pinned formula)
echo /opt/homebrew/Cellar/openjdk@17/$(ls /opt/homebrew/Cellar/openjdk@17)/libexec/openjdk.jdk/Contents/Home

# JDK 25 (latest formula)
echo /opt/homebrew/Cellar/openjdk/$(ls /opt/homebrew/Cellar/openjdk)/libexec/openjdk.jdk/Contents/Home
```

---

## 3. Add JDKs to jenv

```bash
# Install jenv if not already installed
brew install jenv

# Add jenv to your shell (~/.zshrc or ~/.bashrc)
export PATH="$HOME/.jenv/bin:$PATH"
eval "$(jenv init -)"

# Add a JDK (paste the JAVA_HOME path from step 2)
jenv add /opt/homebrew/Cellar/openjdk@17/17.0.19/libexec/openjdk.jdk/Contents/Home
jenv add /opt/homebrew/Cellar/openjdk/25.0.2/libexec/openjdk.jdk/Contents/Home

# Verify
jenv versions
```

---

## 4. Switch JDK Version

| Scope | Command |
|-------|---------|
| Global (system default) | `jenv global 17` |
| Per project (writes `.java-version`) | `jenv local 17` |
| Current shell session only | `jenv shell 17` |

```bash
# Check which version is active
jenv version
java -version
```

---

## 5. Optional: Symlink into /Library/Java/JavaVirtualMachines

Only needed for IDEs (IntelliJ, VS Code) that scan that directory:

```bash
sudo ln -sfn \
  /opt/homebrew/Cellar/openjdk@17/17.0.19/libexec/openjdk.jdk \
  /Library/Java/JavaVirtualMachines/openjdk-17.jdk

# Now java_home -V will show it
/usr/libexec/java_home -V
```

---

## 6. One-liner: Auto-add All Homebrew JDKs to jenv

```bash
find /opt/homebrew/Cellar -maxdepth 4 -name "Contents" -path "*/openjdk*/libexec/*" 2>/dev/null \
  | while read p; do jenv add "$p/Home"; done
```

---

## Quick Reference

```
# List all jenv-managed versions
jenv versions

# Remove a version from jenv
jenv remove openjdk64-17.0.19

# Enable JAVA_HOME export plugin (important for Maven/Gradle)
jenv enable-plugin export

# Rehash after adding new JDKs
jenv rehash
```
