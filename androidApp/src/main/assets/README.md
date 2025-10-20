# AI Model Setup

This directory should contain the on-device AI model for Luminate My Story.

## Required Model

**Model**: Gemma 2B Uncensored (Q8_0 Quantized)
**File**: `gemma-2b-uncensored-v1.Q8_0.gguf`
**Size**: ~2.78 GB
**Source**: https://huggingface.co/sirev/Gemma-2b-Uncensored-v1-Q8_0-GGUF

## Download Instructions

### Option 1: Manual Download
1. Visit https://huggingface.co/sirev/Gemma-2b-Uncensored-v1-Q8_0-GGUF
2. Download the file: `gemma-2b-uncensored-v1-q8_0.gguf`
3. Place the file in this directory (`androidApp/src/main/assets/`)

### Option 2: Using Hugging Face CLI
```bash
# Install huggingface-hub if not already installed
pip install huggingface-hub

# Download the model
huggingface-cli download sirev/Gemma-2b-Uncensored-v1-Q8_0-GGUF \
  gemma-2b-uncensored-v1-q8_0.gguf \
  --local-dir androidApp/src/main/assets/
```

### Option 3: Using Git LFS
```bash
cd androidApp/src/main/assets/
git lfs install
git clone https://huggingface.co/sirev/Gemma-2b-Uncensored-v1-Q8_0-GGUF
mv Gemma-2b-Uncensored-v1-Q8_0-GGUF/gemma-2b-uncensored-v1-q8_0.gguf .
rm -rf Gemma-2b-Uncensored-v1-Q8_0-GGUF
```

## Important Notes

⚠️ **Large File Warning**: This model is 2.78 GB. Ensure you have sufficient disk space and a stable internet connection.

⚠️ **Git Ignore**: This model file should NOT be committed to git due to its size. It's already added to `.gitignore`.

✅ **After Download**: Once the model is in place, the Android app will automatically load it on startup.

## Verification

After downloading, verify the file is in the correct location:
```bash
ls -lh androidApp/src/main/assets/gemma-2b-uncensored-v1.Q8_0.gguf
```

Expected output: A file of approximately 2.78 GB in size.
